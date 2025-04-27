package com.wait.app.repository;

import com.tangzc.mpe.base.repository.BaseRepository;
import com.wait.app.domain.entity.PetMedicalRecords;
import com.wait.app.repository.mapper.PetMedicalRecordsMapper;
import org.springframework.stereotype.Repository;

/**
 * @author 天
 * Time: 2025/4/27 22:03
 */
@Repository
public class PetMedicalRecordsRepository extends BaseRepository<PetMedicalRecordsMapper, PetMedicalRecords> {
}
