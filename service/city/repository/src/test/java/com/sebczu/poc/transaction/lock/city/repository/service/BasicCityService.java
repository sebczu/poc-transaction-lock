package com.sebczu.poc.transaction.lock.city.repository.service;

import com.sebczu.poc.transaction.lock.city.repository.CityRepository;
import com.sebczu.poc.transaction.lock.city.repository.entity.CityEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasicCityService {

    public static final int POPULATION_TO_ADD = 1;
    private final CityRepository repository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void showVersion(int id) {
        CityEntity city = repository.findById(id)
            .orElseThrow();
        log.info("version: " + city.getVersion());
    }

}
