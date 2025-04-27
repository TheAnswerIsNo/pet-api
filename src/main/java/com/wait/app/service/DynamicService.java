package com.wait.app.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.wait.app.domain.dto.dynamic.DynamicListDTO;
import com.wait.app.domain.entity.Attachment;
import com.wait.app.domain.entity.Dynamic;
import com.wait.app.domain.enumeration.AttachmentEnum;
import com.wait.app.domain.param.dynamic.DynamicAddParam;
import com.wait.app.repository.DynamicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * @author 天
 * Time: 2025/4/24 20:41
 */
@Service
public class DynamicService {

    private final DynamicRepository dynamicRepository;

    private final AttachmentService attachmentService;

    @Autowired
    public DynamicService(DynamicRepository dynamicRepository, AttachmentService attachmentService) {
        this.dynamicRepository = dynamicRepository;
        this.attachmentService = attachmentService;
    }

    @Transactional(rollbackFor = Exception.class)
    public void add(DynamicAddParam dynamicAddParam, String userId) {
        Dynamic dynamic = BeanUtil.copyProperties(dynamicAddParam, Dynamic.class);
        dynamic.setUserId(userId);
        dynamicRepository.save(dynamic);
        // 添加图片
        attachmentService.uploadAttachment(dynamicAddParam.getPhoto(),AttachmentEnum.DYNAMIC.getValue(),dynamic.getId());
    }

    public List<DynamicListDTO> list(Integer type,String userId) {
        // 计算三天前的时间
        LocalDateTime threeDaysAgo = LocalDateTime.now().minusDays(3);
        List<Dynamic> dynamicList = dynamicRepository.lambdaQuery()
                .eq(type.equals(1),Dynamic::getUserId,userId)
                // 添加时间筛选条件，只查询三天内的数据
                .ge(Dynamic::getCreateTime, threeDaysAgo)
                .orderByDesc(Dynamic::getLikeNum).list();

        if (CollUtil.isEmpty(dynamicList)){
            return List.of();
        }

        List<String> dynamicIds = dynamicList.stream().map(Dynamic::getId).toList();
        Map<String, List<Attachment>> attachmentMap = attachmentService.getAttachment(AttachmentEnum.DYNAMIC.getValue(), dynamicIds)
                .stream().collect(Collectors.groupingBy(Attachment::getOwnerId));
        return dynamicList.stream().map(item ->{
            DynamicListDTO dynamicListDTO = BeanUtil.copyProperties(item, DynamicListDTO.class);
            List<Attachment> attachmentList = attachmentMap.getOrDefault(item.getId(), List.of());
            if (CollUtil.isNotEmpty(attachmentList)){
                String url = attachmentService.getAttachmentUrl(attachmentList.get(0).getObjName(), null);
                dynamicListDTO.setPhoto(url);
            }
            return dynamicListDTO;
        }).toList();
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(String id) {
        dynamicRepository.removeById(id);
        attachmentService.removeAttachment(AttachmentEnum.DYNAMIC.getValue(), id);
    }

    @Transactional(rollbackFor = Exception.class)
    public void like(String id) {
        Dynamic dynamic = dynamicRepository.getById(id);
        dynamic.setLikeNum(dynamic.getLikeNum()+1);
        dynamicRepository.updateById(dynamic);
    }
}
