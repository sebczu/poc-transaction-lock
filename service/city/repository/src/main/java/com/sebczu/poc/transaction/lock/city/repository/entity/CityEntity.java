package com.sebczu.poc.transaction.lock.city.repository.entity;

import javax.persistence.Version;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "city")
@Entity(name = "City")
@ToString
public class CityEntity {

    @Id
    private Integer id;
    @Version
    private Integer version;
    private String name;
    private Integer population;

    public CityEntity(Integer id, String name, Integer population) {
        this.id = id;
        this.name = name;
        this.population = population;
    }

}
