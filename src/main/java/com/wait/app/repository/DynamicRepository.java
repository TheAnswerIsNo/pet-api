package com.wait.app.repository;

import com.tangzc.mpe.base.repository.BaseRepository;
import com.wait.app.domain.entity.Dynamic;
import com.wait.app.repository.mapper.DynamicMapper;
import org.springframework.stereotype.Repository;

/**
 *
 * @author 天
 * Time: 2025/4/24 20:42
 */
@Repository
public class DynamicRepository extends BaseRepository<DynamicMapper, Dynamic> {
}
