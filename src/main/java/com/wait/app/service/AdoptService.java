package com.wait.app.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.wait.app.domain.dto.adopt.AdoptListDTO;
import com.wait.app.domain.entity.ApplyAdoptRecord;
import com.wait.app.domain.entity.Attachment;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    /**
     * 领养列表
     * @param type type
     * @return List<AdoptListDTO>
     */
    public List<AdoptListDTO> list(String type) {
        List<Pet> petList = petRepository.lambdaQuery().eq(StrUtil.isNotBlank(type), Pet::getType, type)
                .orderByDesc(Pet::getCreateTime)
                .list();
        if (CollUtil.isEmpty(petList)){
            return List.of();
        }
        List<String> petIds = petList.stream().map(Pet::getId).toList();
        Map<String, List<GiveUpAdoptRecord>> giveUpAdoptRecordMap = giveUpAdoptRecordRepository.lambdaQuery().in(GiveUpAdoptRecord::getPetId, petIds)
                .orderByDesc(GiveUpAdoptRecord::getCreateTime)
                .list().stream().collect(Collectors.groupingBy(GiveUpAdoptRecord::getPetId));
        List<AdoptListDTO> result = new ArrayList<>();
        // 获取图片
        Map<String, List<Attachment>> attachmentMap = attachmentService.getAttachment(AttachmentEnum.PET.getValue(), petIds)
                .stream().collect(Collectors.groupingBy(Attachment::getOwnerId));
        for (Pet pet : petList) {
            AdoptListDTO adoptListDTO = BeanUtil.copyProperties(pet, AdoptListDTO.class);
            adoptListDTO.setCharacteristics(JSONUtil.toList(pet.getCharacteristics(), String.class));
            GiveUpAdoptRecord giveUpAdoptRecord = giveUpAdoptRecordMap.get(pet.getId()).get(0);
            adoptListDTO.setGiveUpAdoptRecordId(giveUpAdoptRecord.getId());
            adoptListDTO.setDescription(giveUpAdoptRecord.getDescription());
            adoptListDTO.setUserId(giveUpAdoptRecord.getUserId());
            // 添加图片
            List<String> photos = new ArrayList<>();
            List<Attachment> attachmentList = attachmentMap.get(pet.getId());
            for (Attachment attachment : attachmentList) {
                String url = attachmentService.getAttachmentUrl(attachment.getObjName(), null);
                photos.add(url);
            }
            adoptListDTO.setPhotos(photos);
            result.add(adoptListDTO);
        }
        return result;
    }
}
