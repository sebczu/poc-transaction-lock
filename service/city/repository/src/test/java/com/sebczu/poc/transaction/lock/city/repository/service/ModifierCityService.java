package com.sebczu.poc.transaction.lock.city.repository.service;

import com.sebczu.poc.transaction.lock.city.repository.OptimisticCityRepository;
import com.sebczu.poc.transaction.lock.city.repository.PessimisticReadCityRepository;
import com.sebczu.poc.transaction.lock.city.repository.PessimisticWriteCityRepository;
import com.sebczu.poc.transaction.lock.city.repository.entity.CityEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ModifierCityService {

    private final OptimisticCityRepository optimisticCityRepository;
    private final PessimisticReadCityRepository pessimisticReadCityRepository;
    private final PessimisticWriteCityRepository pessimisticWriteCityRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public CityEntity optimistic_addPopulation(int id) {
        CityEntity city = optimisticCityRepository.findById(id)
            .orElseThrow();
        log.info("modifier version: " + city.getVersion());
        city.setPopulation(city.getPopulation() + BasicCityService.POPULATION_TO_ADD);
        return optimisticCityRepository.save(city);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, timeout = 5)
    public CityEntity pessimisticRead_addPopulation(int id) {
        CityEntity city = pessimisticReadCityRepository.findById(id)
            .orElseThrow();
        log.info("modifier version: " + city.getVersion());
        city.setPopulation(city.getPopulation() + BasicCityService.POPULATION_TO_ADD);
        return pessimisticReadCityRepository.save(city);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, timeout = 5)
    public CityEntity pessimisticWrite_addPopulation(int id) {
        CityEntity city = pessimisticWriteCityRepository.findById(id)
            .orElseThrow();
        log.info("modifier version: " + city.getVersion());
        city.setPopulation(city.getPopulation() + BasicCityService.POPULATION_TO_ADD);
        return pessimisticWriteCityRepository.save(city);
    }

}
