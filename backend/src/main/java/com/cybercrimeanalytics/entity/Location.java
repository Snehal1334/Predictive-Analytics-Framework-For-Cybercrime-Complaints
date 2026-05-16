package com.cybercrimeanalytics.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "locations", indexes = {
        @Index(name = "idx_location_city_state", columnList = "city,state"),
        @Index(name = "idx_location_coordinates", columnList = "latitude,longitude")
})
public class Location extends BaseEntity {
    @Column(nullable = false, length = 180)
    private String address;

    @Column(length = 80)
    private String city;

    @Column(length = 80)
    private String state;

    @Column(length = 20)
    private String postalCode;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;
}
