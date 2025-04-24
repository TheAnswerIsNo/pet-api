package com.wait.app.service;

import cn.dev33.satoken.exception.SaTokenException;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
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
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

/**
 *
 * @author 天
 * Time: 2025/4/23 23:31
 */
@Service
@Log4j2
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

    /**
     * 宠物识别
     * @param photo photo
     * @return String
     */
    public String identification(MultipartFile photo) {
        try {
            // 获取图片base64
            String base64 = Base64.getEncoder().encodeToString(photo.getBytes());

            String url = "https://jmdwsbocr.market.alicloudapi.com/image/recognition/animal";
            String appcode = "26ce0a654aa542ea96c779ca367b54cd";
            Map<String, String> query = new HashMap<>();
            query.put("base64", base64);
            HttpResponse response = HttpUtil.createPost(url)
                    .header("Authorization", "APPCODE " + appcode)
                    .header("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                    .body(JSONUtil.toJsonStr(query)).execute();
            // {
            //  "code": 200,//返回码，详见返回码说明
            //  "msg": "成功",//返回码对应描述
            //  "taskNo": "495451177190764093826703",//本次请求号
            //  "data": {
            //    "results": [
            //      {
            //        "score": "0.565209",//置信度
            //        "name": "苏门答腊虎" //动物名称
            //      }
            //    ]
            //  }
            //}
            Map<String, Object> responseMap = JSONUtil.toBean(response.body(), Map.class);
            if (Integer.parseInt(responseMap.get("code").toString()) == 400){
                throw new SaTokenException(400,"识别图片不准确");
            }else if (Integer.parseInt(responseMap.get("code").toString()) == 500){
                throw new SaTokenException(500,"识别系统异常");
            }
            return responseMap.get("name").toString();
        }catch (Exception e){
            log.error("宠物识别失败"+e.getMessage(),e);
            throw new SaTokenException("宠物识别失败");
        }

    }
}
