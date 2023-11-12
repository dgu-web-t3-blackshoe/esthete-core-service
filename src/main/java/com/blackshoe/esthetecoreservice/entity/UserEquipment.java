package com.blackshoe.esthetecoreservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "user_equipments")
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Getter
@Builder
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class UserEquipment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "equipment_id")
    private Long id;

    @Column(name = "equipment_name", nullable = false, length = 50)
    private String userEquipmentName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "user_equipment_fk_user_id"))
    private User user;
}
