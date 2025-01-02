package com.wait.app.controller.system;


import cn.dev33.satoken.util.SaResult;
import com.wait.app.domain.dto.user.UserInfoDTO;
import com.wait.app.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 天
 * Time: 2024/10/28 18:19
 */
@RestController
@RequestMapping("/user")
@Api(tags = "用户",value = "用户")
public class UserController extends BaseController{

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @ApiOperation(value = "用户信息")
    @GetMapping("/info")
    public SaResult info(){
        UserInfoDTO userInfoDTO = userService.info();
        return SaResult.data(userInfoDTO);
    }


}
