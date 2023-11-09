package com.blackshoe.esthetecoreservice.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Entity
@Table(name = "equipments")
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Getter
@Builder
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Equipment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "equipment_id")
    private Long id;

    @Column(name = "equipment", nullable = false, length = 20)
    private String equipment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "photo_id", foreignKey = @ForeignKey(name = "equipment_fk_photo_id"))
    private Photo photo;
}

