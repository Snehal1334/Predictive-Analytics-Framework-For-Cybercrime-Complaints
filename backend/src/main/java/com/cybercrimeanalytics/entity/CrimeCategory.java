package com.cybercrimeanalytics.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "crime_categories")
public class CrimeCategory extends BaseEntity {
    @Column(nullable = false, unique = true, length = 80)
    private String name;

    @Column(length = 500)
    private String description;
}
