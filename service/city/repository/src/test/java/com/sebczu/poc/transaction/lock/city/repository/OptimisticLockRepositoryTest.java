package com.sebczu.poc.transaction.lock.city.repository;

import com.sebczu.poc.transaction.lock.city.repository.entity.CityEntity;
import com.sebczu.poc.transaction.lock.city.repository.factory.CityFactory;
import com.sebczu.poc.transaction.lock.city.repository.service.BasicCityService;
import com.sebczu.poc.transaction.lock.city.repository.service.TransactionCityService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

class OptimisticLockRepositoryTest extends CityRepositoryTest {

    @Autowired
    private BasicCityService service;
    @Autowired
    private TransactionCityService cityService;

    //T1 ---(startTransaction)---(version=0)------------------------------------(update entity)------------------(incorrect version | rollback)
    //T2 ---(startTransaction)------------------(version=0)---(update entity)------( version=1 | endTransaction)
    @Test
    void readPopulationT1_addPopulationT2_addPopulationT1() {
        CityEntity city = repository.save(CityFactory.create());

        Throwable thrown = catchThrowable(() -> cityService.optimistic_readPopulation_wait_addPopulation(city.getId()));

        assertThat(thrown)
            .isInstanceOf(ObjectOptimisticLockingFailureException.class);

        assertThat(repository.findAll())
            .hasSize(1)
            .element(0)
            .extracting(CityEntity::getPopulation, CityEntity::getVersion)
            .containsExactly(1, 1);
    }

}
