package com.sebczu.poc.transaction.lock.city.repository;

import com.sebczu.poc.transaction.lock.city.repository.entity.CityEntity;
import java.util.Optional;
import javax.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

public interface PessimisticWriteCityRepository extends JpaRepository<CityEntity, Integer> {

    @Override
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<CityEntity> findById(Integer id);

}
