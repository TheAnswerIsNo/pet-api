package com.wait.app.repository;

import com.tangzc.mpe.base.repository.BaseRepository;
import com.wait.app.domain.entity.Address;
import com.wait.app.repository.mapper.AddressMapper;
import org.springframework.stereotype.Repository;

/**
 * @author 天
 * Time: 2024/10/16 13:45
 */
@Repository
public class AddressRepository extends BaseRepository<AddressMapper, Address> {
}
