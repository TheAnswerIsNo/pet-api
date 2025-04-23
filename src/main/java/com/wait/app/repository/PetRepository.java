package com.wait.app.repository;

import com.tangzc.mpe.base.repository.BaseRepository;
import com.wait.app.domain.entity.Pet;
import com.wait.app.repository.mapper.PetMapper;
import org.springframework.stereotype.Repository;

/**
 * @author 天
 * Time: 2025/4/24 0:58
 */
@Repository
public class PetRepository extends BaseRepository<PetMapper, Pet> {
}
