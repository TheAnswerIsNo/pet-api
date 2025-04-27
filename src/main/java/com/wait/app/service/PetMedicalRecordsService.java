package com.wait.app.service;

import cn.hutool.core.bean.BeanUtil;
import com.wait.app.domain.dto.petMedicalRecords.PetMedicalRecordsListDTO;
import com.wait.app.domain.entity.PetMedicalRecords;
import com.wait.app.domain.param.petMedicalRecords.PetMedicalRecordsSaveParam;
import com.wait.app.repository.PetMedicalRecordsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 *
 * @author 天
 * Time: 2025/4/27 21:57
 */
@Service
public class PetMedicalRecordsService {


    private final PetMedicalRecordsRepository petMedicalRecordsRepository;

    @Autowired
    public PetMedicalRecordsService(PetMedicalRecordsRepository petMedicalRecordsRepository) {
        this.petMedicalRecordsRepository = petMedicalRecordsRepository;
    }

    @Transactional(rollbackFor = Exception.class)
    public void save(PetMedicalRecordsSaveParam petMedicalRecordsSaveParam,String userId) {
        PetMedicalRecords petMedicalRecords = BeanUtil.copyProperties(petMedicalRecordsSaveParam, PetMedicalRecords.class);
        petMedicalRecords.setUserId(userId);
        petMedicalRecordsRepository.saveOrUpdate(petMedicalRecords);

    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(String id) {
        petMedicalRecordsRepository.removeById(id);
    }

    public List<PetMedicalRecordsListDTO> list(String userId) {
        List<PetMedicalRecords> records = petMedicalRecordsRepository.lambdaQuery().eq(PetMedicalRecords::getUserId, userId).list();
        return BeanUtil.copyToList(records, PetMedicalRecordsListDTO.class);
    }
}
