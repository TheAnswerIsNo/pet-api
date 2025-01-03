package com.wait.app.service;

import cn.dev33.satoken.exception.SaTokenException;
import cn.dev33.satoken.secure.BCrypt;
import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

import com.wait.app.domain.dto.role.RoleDTO;
import com.wait.app.domain.dto.user.UserInfoDTO;
import com.wait.app.domain.dto.wechat.WeChatAccessTokenDTO;
import com.wait.app.domain.dto.wechat.WeChatLoginEmpowerDTO;
import com.wait.app.domain.dto.wechat.WechatUserInfoDTO;
import com.wait.app.domain.entity.User;
import com.wait.app.domain.enumeration.*;
import com.wait.app.domain.param.login.WebLoginParam;
import com.wait.app.domain.param.login.WechatLoginParam;
import com.wait.app.repository.RoleRepository;
import com.wait.app.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * @author 天
 * Time: 2024/9/6 22:21
 */
@Service
@Slf4j
public class LoginService {
    private final UserRepository userRepository;

    private final UserService userService;

    private final RoleRepository roleRepository;

    private final Executor executor;

    @Autowired
    public LoginService(UserRepository userRepository, Executor executor, UserService userService, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.executor = executor;
        this.userService = userService;
        this.roleRepository = roleRepository;
    }

    /**
     * 微信登录
     *
     * @param wechatLoginParam wechatLoginParam
     * @return 用户信息
     */
    @Transactional(rollbackFor = Exception.class)
    public UserInfoDTO wechatLogin(WechatLoginParam wechatLoginParam) {
        //请求授权，获取用户唯一openid、unionId、session_key
        long begin = System.currentTimeMillis();
        String s = HttpRequest.get(WeChatURLEnum.WECHAT_LOGIN_URL.getName())
                .header(Header.USER_AGENT, "Mozilla/5.0")
                .form(WeChatInfoEnum.APPID.getName(), WeChatInfoEnum.APPID.getValue())
                .form(WeChatInfoEnum.SECRET.getName(), WeChatInfoEnum.SECRET.getValue())
                .form(WeChatInfoEnum.JSCODE.getName(), wechatLoginParam.getCode())
                .form(WeChatInfoEnum.GRANT_TYPE_LOGIN.getName(), WeChatInfoEnum.GRANT_TYPE_LOGIN.getValue())
                .execute().body();
        long end = System.currentTimeMillis();
        log.info(String.format("请求openid服务 -> code编码：%s；总耗时：%d(ms)",wechatLoginParam.getCode(), end - begin));

        WeChatLoginEmpowerDTO weChatLoginEmpowerDTO = JSONUtil.toBean(s, WeChatLoginEmpowerDTO.class);
        log.info(String.format("请求openid服务结果 -> openid：%s；unionid：%s；session_key：%s；错误信息：%s；", weChatLoginEmpowerDTO.getOpenid(),
                weChatLoginEmpowerDTO.getUnionid(), weChatLoginEmpowerDTO.getSessionKey(), weChatLoginEmpowerDTO.getErrmsg()));

        //获取绑定手机号请求所需的access_token
        begin = System.currentTimeMillis();
        String token = HttpRequest.get(WeChatURLEnum.WECHAT_ACCESS_TOKEN.getName())
                .header(Header.USER_AGENT, "Mozilla/5.0")
                .form(WeChatInfoEnum.GRANT_TYPE_TOKEN.getName(), WeChatInfoEnum.GRANT_TYPE_TOKEN.getValue())
                .form(WeChatInfoEnum.APPID.getName(), WeChatInfoEnum.APPID.getValue())
                .form(WeChatInfoEnum.SECRET.getName(), WeChatInfoEnum.SECRET.getValue())
                .execute().body();
        end = System.currentTimeMillis();
        log.info(String.format("请求access_token服务 -> 总耗时：%d(ms)", end - begin));

        WeChatAccessTokenDTO weChatAccessTokenDTO = JSONUtil.toBean(token, WeChatAccessTokenDTO.class);
        log.info(String.format("请求access_token服务服务结果 -> access_token码：%s；有效时间：%s",
                weChatAccessTokenDTO.getAccessToken(),weChatAccessTokenDTO.getExpiresIn()));

        //获取手机号
        HashMap<String, String> map = new HashMap<>();
        map.put(WeChatInfoEnum.CODE.getName(), wechatLoginParam.getPhoneCode());
        String jsonPrettyStr = JSONUtil.toJsonPrettyStr(map);
        begin = System.currentTimeMillis();
        String body = HttpRequest.post(WeChatURLEnum.WECHAT_PHONE.getName()+weChatAccessTokenDTO.getAccessToken())
                .body(jsonPrettyStr).execute().body();
        end = System.currentTimeMillis();
        log.info(String.format("请求电话号码服务 -> 花费时间：%s",end - begin));

        JSONObject entries = JSONUtil.parseObj(body);
        JSONObject phoneInfo = entries.getJSONObject("phone_info");
        String phoneNumber = phoneInfo.getStr("phoneNumber");
        log.info(String.format("请求电话号服务 -> 电话号码：%s;",phoneNumber));
        User userByOpenId = userRepository.getUserByOpenId(weChatLoginEmpowerDTO.getOpenid());
        User userByPhone = userRepository.getUserByPhone(phoneNumber);
        User user = User.builder()
                .phone(phoneNumber)
                .openId(weChatLoginEmpowerDTO.getOpenid())
                .build();
        if (ObjUtil.isNull(userByOpenId) && ObjUtil.isNull(userByPhone)){
            userRepository.save(user);
            userService.distribute(user.getId(), List.of(roleRepository.getRoleByLevel(RoleKeyEnum.USER.getLevel()).getId()));
        }else if (Objects.isNull(userByOpenId)){
            user.setId(userByPhone.getId());
            userRepository.updateById(user);
        } else if(Objects.nonNull(userByPhone)){
            BeanUtil.copyProperties(userByOpenId, user);
        }
        // 异步获取用户信息
        CompletableFuture.runAsync(()->{
            long sbegin = System.currentTimeMillis();
            String wechatUserInfo = HttpRequest.get(WeChatURLEnum.WECHAT_USER_INFO.getName())
                    .header(Header.USER_AGENT, "Mozilla/5.0")
                    .form(WeChatInfoEnum.ACCESS_TOKEN.getName(),weChatAccessTokenDTO.getAccessToken() )
                    .form(WeChatInfoEnum.OPENID.getName(), weChatLoginEmpowerDTO.getOpenid())
                    .execute().body();
            long send = System.currentTimeMillis();
            log.info(String.format("请求userInfo服务 -> 总耗时：%d(ms)", send - sbegin));
            WechatUserInfoDTO wechatUserInfoDTO = JSONUtil.toBean(wechatUserInfo, WechatUserInfoDTO.class);
            log.info(String.format("请求userInfo服务服务结果 -> nickname：%s；sex: %s", wechatUserInfoDTO.getNickname(),wechatUserInfoDTO.getSex()));
            userRepository.updateById(User.builder()
                    .id(user.getId())
                    .nickname(wechatUserInfoDTO.getNickname())
                    .sex(wechatUserInfoDTO.getSex())
                    .build());
        },executor);
        return publicLogin(user, DeviceEnum.APPLET.getValue());
    }

    /**
     * 公用登录方法
     */
    private UserInfoDTO publicLogin(User user, String device) {
        String userId = user.getId();
        String loginId = PrefixEnum.USER.getValue() + userId;
        StpUtil.login(loginId, device);
        UserInfoDTO userInfoDTO = BeanUtil.copyProperties(user, UserInfoDTO.class);
        List<RoleDTO> roleDTOList = roleRepository.getRoleByUserId(userId);
        // 异步序列化进redis
        CompletableFuture.runAsync(() -> {
            SaSession session = StpUtil.getSessionByLoginId(loginId);
            // 添加角色
            session.set(SaSession.USER, JSONUtil.toJsonStr(user)).
                    set(SaSession.ROLE_LIST, JSONUtil.toJsonStr(roleDTOList));
        },executor);
        userInfoDTO.setRoles(roleDTOList);
        return userInfoDTO;
    }

    /**
     * 注销
     */
    public void logout() {
        StpUtil.logout();
    }

    public UserInfoDTO webLogin(WebLoginParam webLoginParam) {
        String phone = webLoginParam.getPhone();
        String password = webLoginParam.getPassword();
        User user = userService.getUserByPhone(phone);
        if (Objects.isNull(user)) {
            throw new SaTokenException(400, "该手机号不存在");
        }

        if (!BCrypt.checkpw(password, user.getPassword())) {
            throw new SaTokenException(400, "密码错误");
        }
        return this.publicLogin(user, DeviceEnum.WEB.getValue());
    }
}
