package com.sebczu.poc.transaction.lock.city.repository;

import com.sebczu.poc.transaction.lock.city.repository.entity.CityEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CityRepository extends JpaRepository<CityEntity, Integer> {

}
