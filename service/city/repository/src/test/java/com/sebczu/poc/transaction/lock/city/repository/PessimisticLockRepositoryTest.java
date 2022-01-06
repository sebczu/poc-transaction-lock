package com.sebczu.poc.transaction.lock.city.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.sebczu.poc.transaction.lock.city.repository.entity.CityEntity;
import com.sebczu.poc.transaction.lock.city.repository.factory.CityFactory;
import com.sebczu.poc.transaction.lock.city.repository.service.BasicCityService;
import com.sebczu.poc.transaction.lock.city.repository.service.TransactionCityService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class PessimisticLockRepositoryTest extends CityRepositoryTest {

    @Autowired
    private BasicCityService service;
    @Autowired
    private TransactionCityService cityService;

    //T1 ---(startTransaction)---(version=0)---------------------------------------(update entity)------------------(save | endTransaction)
    //T2 ---(startTransaction)------------------(version=0)---(update entity)------( wait for unlock | endTransaction or rollback)
    @Test
    void pessimisticRead_readPopulationT1_addPopulationT2_addPopulationT1() {
        CityEntity city = repository.save(CityFactory.create());

        cityService.pessimisticRead_readPopulation_wait_addPopulation(city.getId());

        assertThat(repository.findAll())
            .hasSize(1)
            .element(0)
            .extracting(CityEntity::getPopulation, CityEntity::getVersion)
            .containsExactly(1, 1);
    }

    //T1 ---(startTransaction)---(version=0)---------------------------------------(update entity)------------------(save | endTransaction)
    //T2 ---(startTransaction)------------------( wait for unlock | rollback)
    @Test
    void pessimisticWrite_readPopulationT1_addPopulationT2_addPopulationT1() {
        CityEntity city = repository.save(CityFactory.create());

        cityService.pessimisticWrite_readPopulation_wait_addPopulation(city.getId());

        assertThat(repository.findAll())
            .hasSize(1)
            .element(0)
            .extracting(CityEntity::getPopulation, CityEntity::getVersion)
            .containsExactly(1, 1);
    }
}
