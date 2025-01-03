package com.wait.app.controller.system;

import cn.dev33.satoken.annotation.SaIgnore;

import cn.dev33.satoken.util.SaResult;
import com.wait.app.domain.dto.user.UserInfoDTO;
import com.wait.app.domain.param.login.WebLoginParam;
import com.wait.app.domain.param.login.WechatLoginParam;
import com.wait.app.service.LoginService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 *
 * @author 天
 * Time: 2024/9/6 22:19
 */
@RestController
@Api(tags = "登录",value = "登录")
public class LoginController extends BaseController{

    private final LoginService loginService;

    @Autowired
    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @SaIgnore
    @ApiOperation(value = "微信登录")
    @PostMapping("/login/wechat")
    public SaResult wechatLogin(@RequestBody @NonNull WechatLoginParam wechatLoginParam){
        UserInfoDTO userInfoDTO  = loginService.wechatLogin(wechatLoginParam);
        return new SaResult(SaResult.CODE_SUCCESS,"登录成功",userInfoDTO);
    }

    @SaIgnore
    @ApiOperation("后台登录")
    @PostMapping("/login/web")
    public SaResult login(@RequestBody WebLoginParam webLoginParam){
        UserInfoDTO userInfoDTO = loginService.webLogin(webLoginParam);
        return new SaResult(SaResult.CODE_SUCCESS,"登录成功",userInfoDTO);
    }

    @ApiOperation(value = "注销")
    @RequestMapping(method = RequestMethod.POST,value = "/logout")
    public SaResult logout(){
        loginService.logout();
        return SaResult.ok("注销成功");
    }


}
