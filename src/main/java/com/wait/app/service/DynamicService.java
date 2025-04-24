package com.wait.app.service;

import cn.hutool.core.bean.BeanUtil;
import com.wait.app.domain.entity.Dynamic;
import com.wait.app.domain.param.dynamic.DynamicAddParam;
import com.wait.app.repository.DynamicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author 天
 * Time: 2025/4/24 20:41
 */
@Service
public class DynamicService {

    private final DynamicRepository dynamicRepository;

    @Autowired
    public DynamicService(DynamicRepository dynamicRepository) {
        this.dynamicRepository = dynamicRepository;
    }

    @Transactional(rollbackFor = Exception.class)
    public void add(DynamicAddParam dynamicAddParam, String userId) {
        Dynamic dynamic = BeanUtil.copyProperties(dynamicAddParam, Dynamic.class);
        dynamic.setUserId(userId);
        dynamicRepository.save(dynamic);
    }
}
