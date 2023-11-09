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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "photo_id")
    private Long id;

    @Column(name = "longitude", nullable = false, length = 20)
    private Double longitude;

    @Column(name = "latitude", nullable = false, length = 20)
    private Double latitude;

    @Column(name = "state", nullable = false, length = 20)
    private String state;

    @Column(name = "city", nullable = false, length = 20)
    private String city;

    @Column(name = "town", nullable = false, length = 20)
    private String town;
}
