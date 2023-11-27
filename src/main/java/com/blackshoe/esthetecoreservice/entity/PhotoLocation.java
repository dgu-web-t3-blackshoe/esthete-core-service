package com.blackshoe.esthetecoreservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Entity
@Table(name = "photo_locations")
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Getter
@Builder @AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class PhotoLocation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "photo_location_id")
    private Long id;

    @Column(name = "longitude",  length = 20)
    private Double longitude;

    @Column(name = "latitude",  length = 20)
    private Double latitude;

    @Column(name = "state",  length = 20)
    private String state;

    @Column(name = "city",  length = 20)
    private String city;

    @Column(name = "town",  length = 20)
    private String town;

}
