package com.wait.app.controller;

import cn.dev33.satoken.util.SaResult;
import com.wait.app.domain.dto.petMedicalRecords.PetMedicalRecordsListDTO;
import com.wait.app.domain.param.petMedicalRecords.PetMedicalRecordsSaveParam;
import com.wait.app.service.PetMedicalRecordsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 天
 * Time: 2025/4/27 21:57
 */
@RestController
@Api(tags = "宠物医疗记录",value = "宠物医疗记录")
@RequestMapping("/petMedicalRecords")
public class PetMedicalRecordsController extends BaseController{

    private final PetMedicalRecordsService petMedicalRecordsService;

    @Autowired
    public PetMedicalRecordsController(PetMedicalRecordsService petMedicalRecordsService) {
        this.petMedicalRecordsService = petMedicalRecordsService;
    }

    @GetMapping(value = "/list")
    @ApiOperation(value = "宠物医疗记录列表")
    public SaResult list(){
        List<PetMedicalRecordsListDTO> list = petMedicalRecordsService.list(getUserId());
        return SaResult.data(list);
    }

    @PostMapping(value = "/save")
    @ApiOperation(value = "保存宠物医疗记录")
    public SaResult save(@RequestBody PetMedicalRecordsSaveParam petMedicalRecordsSaveParam){
        petMedicalRecordsService.save(petMedicalRecordsSaveParam,getUserId());
        return SaResult.ok("保存成功");
    }

    @GetMapping(value = "/delete")
    @ApiOperation(value = "删除宠物医疗记录")
    public SaResult delete(@RequestParam String id){
        petMedicalRecordsService.delete(id);
        return SaResult.ok("删除成功");
    }
}
