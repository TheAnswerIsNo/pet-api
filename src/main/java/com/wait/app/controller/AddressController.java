package com.wait.app.controller;

import cn.dev33.satoken.util.SaResult;
import com.wait.app.domain.dto.address.AddressListDTO;
import com.wait.app.domain.param.address.AddressSaveParam;
import com.wait.app.service.AddressService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @author 天
 * Time: 2024/10/16 13:43
 */
@RestController
@RequestMapping("/address")
@Api(tags = "地址",value = "地址")
public class AddressController extends BaseController {

    private final AddressService addressService;

    @Autowired
    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @ApiOperation(value = "地址列表")
    @GetMapping("/list")
    public SaResult list(){
        List<AddressListDTO> list = addressService.list(getUserId());
        return SaResult.data(list);
    }

    @ApiOperation(value = "地址详情")
    @GetMapping("/info")
    public SaResult info(@RequestParam String id){
        AddressListDTO addressListDTO = addressService.info(id);
        return SaResult.data(addressListDTO);
    }

    @ApiOperation(value = "保存地址")
    @PostMapping("/save")
    public SaResult save(@RequestBody AddressSaveParam addressSaveParam){
        addressService.save(addressSaveParam,getUserId());
        return SaResult.ok("保存成功");
    }

    @ApiOperation(value = "删除地址")
    @PostMapping("/delete")
    public SaResult delete(@RequestParam String id){
        addressService.delete(id);
        return SaResult.ok("删除成功");
    }

}
