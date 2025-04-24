package com.wait.app.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONUtil;
import com.wait.app.domain.entity.ApplyAdoptRecord;
import com.wait.app.domain.entity.GiveUpAdoptRecord;
import com.wait.app.domain.entity.Pet;
import com.wait.app.domain.enumeration.AttachmentEnum;
import com.wait.app.domain.param.adopt.ApplyAdoptParam;
import com.wait.app.domain.param.adopt.GiveUpAdoptParam;
import com.wait.app.repository.ApplyAdoptRecordRepository;
import com.wait.app.repository.GiveUpAdoptRecordRepository;
import com.wait.app.repository.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author 天
 * Time: 2025/4/23 23:31
 */
@Service
public class AdoptService {

    private final PetRepository petRepository;

    private final GiveUpAdoptRecordRepository giveUpAdoptRecordRepository;

    private final AttachmentService attachmentService;

    private final ApplyAdoptRecordRepository applyAdoptRecordRepository;

    @Autowired
    public AdoptService(PetRepository petRepository, GiveUpAdoptRecordRepository giveUpAdoptRecordRepository, AttachmentService attachmentService, ApplyAdoptRecordRepository applyAdoptRecordRepository) {
        this.petRepository = petRepository;
        this.giveUpAdoptRecordRepository = giveUpAdoptRecordRepository;
        this.attachmentService = attachmentService;
        this.applyAdoptRecordRepository = applyAdoptRecordRepository;
    }

    /**
     * 送养宠物
     * @param giveUpAdoptParam giveUpAdoptParam
     * @param userId userId
     */
    @Transactional(rollbackFor = Exception.class)
    public void giveUp(GiveUpAdoptParam giveUpAdoptParam, String userId) {
        Pet pet = BeanUtil.copyProperties(giveUpAdoptParam, Pet.class);
        pet.setCharacteristics(JSONUtil.toJsonStr(giveUpAdoptParam.getCharacteristics()));
        petRepository.save(pet);
        GiveUpAdoptRecord giveUpAdoptRecord = GiveUpAdoptRecord.builder()
                .petId(pet.getId())
                .description(giveUpAdoptParam.getDescription())
                .userId(userId)
                .build();
        giveUpAdoptRecordRepository.save(giveUpAdoptRecord);
        // 保存图片
        if (CollUtil.isNotEmpty(giveUpAdoptParam.getPhotos())){
            attachmentService.uploadAttachmentList(giveUpAdoptParam.getPhotos(), AttachmentEnum.PET.getValue(), pet.getId());
        }
    }

    /**
     * 领养宠物申请
     * @param applyAdoptParam applyAdoptParam
     * @param userId userId
     */
    @Transactional(rollbackFor = Exception.class)
    public void apply(ApplyAdoptParam applyAdoptParam, String userId) {
        ApplyAdoptRecord applyAdoptRecord = BeanUtil.copyProperties(applyAdoptParam, ApplyAdoptRecord.class);
        applyAdoptRecord.setUserId(userId);
        applyAdoptRecordRepository.save(applyAdoptRecord);
    }
}
