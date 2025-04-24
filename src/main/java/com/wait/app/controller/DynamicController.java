package com.wait.app.controller;

import cn.dev33.satoken.util.SaResult;
import com.wait.app.domain.param.dynamic.DynamicAddParam;
import com.wait.app.service.DynamicService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author 天
 * Time: 2025/4/24 20:33
 */
@RestController
@Api(tags = "动态",value = "动态")
@RequestMapping("/dynamic")
public class DynamicController extends BaseController {

    private final DynamicService dynamicService;

    @Autowired
    public DynamicController(DynamicService dynamicService) {
        this.dynamicService = dynamicService;
    }

    @PostMapping(value = "/add")
    @ApiOperation(value = "发布动态")
    public SaResult add(DynamicAddParam dynamicAddParam){
        dynamicService.add(dynamicAddParam,getUserId());
        return SaResult.ok("发布成功");
    }
}
