package com.sebczu.poc.transaction.lock.city.repository.service;

import com.sebczu.poc.transaction.lock.city.repository.OptimisticCityRepository;
import com.sebczu.poc.transaction.lock.city.repository.PessimisticReadCityRepository;
import com.sebczu.poc.transaction.lock.city.repository.PessimisticWriteCityRepository;
import com.sebczu.poc.transaction.lock.city.repository.entity.CityEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionCityService {

    private final OptimisticCityRepository optimisticCityRepository;
    private final PessimisticReadCityRepository pessimisticReadCityRepository;
    private final PessimisticWriteCityRepository pessimisticWriteCityRepository;
    private final ModifierCityService modifierService;
    private final BasicCityService basicService;

    @Transactional
    public CityEntity optimistic_readPopulation_wait_addPopulation(int id) {
        CityEntity city = optimisticCityRepository.findById(id)
            .orElseThrow();
        Integer population = city.getPopulation();
        log.info("version: " + city.getVersion());

        modifierService.optimistic_addPopulation(id);
        log.info("modified success");
        basicService.showVersion(id);

        city.setPopulation(population + BasicCityService.POPULATION_TO_ADD);
        return optimisticCityRepository.save(city);
    }

    @Transactional
    public CityEntity pessimisticWrite_readPopulation_wait_addPopulation(int id) {
        CityEntity city = pessimisticWriteCityRepository.findById(id)
            .orElseThrow();
        Integer population = city.getPopulation();
        log.info("version: " + city.getVersion());

        try {
            modifierService.pessimisticWrite_addPopulation(id);
        } catch (RuntimeException e) {
            log.error("transaction exception", e);
        }
        log.info("modified success");
        basicService.showVersion(id);

        city.setPopulation(population + BasicCityService.POPULATION_TO_ADD);
        return pessimisticWriteCityRepository.save(city);
    }

    @Transactional
    public CityEntity pessimisticRead_readPopulation_wait_addPopulation(int id) {
        CityEntity city = pessimisticReadCityRepository.findById(id)
            .orElseThrow();
        Integer population = city.getPopulation();
        log.info("version: " + city.getVersion());

        try {
            modifierService.pessimisticRead_addPopulation(id);
        } catch (RuntimeException e) {
            log.error("transaction exception", e);
        }
        log.info("modified success");
        basicService.showVersion(id);

        city.setPopulation(population + BasicCityService.POPULATION_TO_ADD);
        return pessimisticReadCityRepository.save(city);
    }
}
