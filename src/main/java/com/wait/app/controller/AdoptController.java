package com.wait.app.controller;

import cn.dev33.satoken.util.SaResult;
import com.wait.app.domain.dto.adopt.AdoptListDTO;
import com.wait.app.domain.param.adopt.ApplyAdoptParam;
import com.wait.app.domain.param.adopt.GiveUpAdoptParam;
import com.wait.app.service.AdoptService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author 天
 * Time: 2025/4/23 14:17
 */
@RestController
@Api(tags = "领养",value = "领养")
@RequestMapping("/adopt")
public class AdoptController extends BaseController{

    private final AdoptService adoptService;

    @Autowired
    public AdoptController(AdoptService adoptService) {
        this.adoptService = adoptService;

    }

    @PostMapping(value = "/give-up")
    @ApiOperation(value = "送养")
    public SaResult giveUp(GiveUpAdoptParam giveUpAdoptParam){
        adoptService.giveUp(giveUpAdoptParam,getUserId());
        return SaResult.ok("发布宠物信息成功");
    }

    @GetMapping(value = "/give-up/delete")
    @ApiOperation(value = "删除送养信息")
    public SaResult giveUpDelete(@RequestParam String id){
        adoptService.giveUpDelete(id);
        return SaResult.ok("删除成功");
    }


    @PostMapping(value = "/apply")
    @ApiOperation(value = "领养")
    public SaResult apply(@RequestBody ApplyAdoptParam applyAdoptParam){
        adoptService.apply(applyAdoptParam,getUserId());
        return SaResult.ok("领养宠物申请成功");
    }

    @GetMapping(value = "/list")
    @ApiOperation(value = "领养列表")
    public SaResult apply(@RequestParam String type,@RequestParam Integer self){
        List<AdoptListDTO> list = adoptService.list(type,self,getUserId());
        return SaResult.data(list);
    }

    @PostMapping(value = "/identification")
    @ApiOperation(value = "宠物识别")
    public SaResult identification(MultipartFile photo){
        List<String> petType = adoptService.identification(photo);
        return SaResult.data(petType);
    }

}
