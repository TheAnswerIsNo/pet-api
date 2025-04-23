package com.wait.app.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wait.app.domain.entity.Pet;
import org.apache.ibatis.annotations.Mapper;

/**
 *
 * @author 天
 * Time: 2025/4/24 0:58
 */
@Mapper
public interface PetMapper extends BaseMapper<Pet> {
}
