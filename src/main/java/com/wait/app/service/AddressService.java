package com.wait.app.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.DesensitizedUtil;

import com.wait.app.domain.dto.address.AddressListDTO;
import com.wait.app.domain.entity.Address;
import com.wait.app.domain.param.address.AddressSaveParam;
import com.wait.app.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author 天
 * Time: 2024/10/16 13:44
 */
@Service
public class AddressService {

    private final AddressRepository addressRepository;

    @Autowired
    public AddressService(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    /**
     * 地址列表
     * @param userId 用户id
     */
    public List<AddressListDTO> list(String userId) {
        List<Address> list = addressRepository.lambdaQuery().eq(Address::getUserId, userId).list();
        List<AddressListDTO> addressList = new ArrayList<>();
        list.forEach(item ->{
            // 手机号脱敏
            item.setPhone(DesensitizedUtil.mobilePhone(item.getPhone()));
            AddressListDTO addressListDTO = BeanUtil.copyProperties(item, AddressListDTO.class);
            addressList.add(addressListDTO);
        });

        return addressList;
    }

    /**
     * 保存地址
     * @param addressSaveParam 保存参数
     * @param userId 用户id
     */
    public void save(AddressSaveParam addressSaveParam, String userId) {
        Address address = BeanUtil.copyProperties(addressSaveParam, Address.class);
        address.setUserId(userId);
        addressRepository.saveOrUpdate(address);
    }

    /**
     * 删除地址
     * @param id id
     */
    public void delete(String id) {
        addressRepository.removeById(id);
    }

    /**
     * 详情
     * @param id id
     */
    public AddressListDTO info(String id) {
        Address address = addressRepository.getById(id);
        return BeanUtil.copyProperties(address, AddressListDTO.class);
    }
}
