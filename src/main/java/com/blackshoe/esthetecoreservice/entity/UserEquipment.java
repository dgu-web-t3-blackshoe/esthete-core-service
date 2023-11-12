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
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class UserEquipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_equipment_id")
    private Long id;


    @Column(name = "equipment_name", nullable = false, length = 50)
    private String userEquipmentName;

    @Column(columnDefinition = "BINARY(16)", name = "user_equipment_uuid")
    private UUID equipmentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "user_equipment_fk_user_id"))
    private User user;

    @Column(name = "equipment_name")
    private String equipmentName;

    @Builder
    public UserEquipment(String equipmentName) {
        this.equipmentName = equipmentName;
    }

    public void setUser(User user) {
        this.user = user;
        user.addUserEquipment(this);
    }

    @PrePersist
    public void setEquipmentId() {
        if (equipmentId == null) {
            equipmentId = UUID.randomUUID();
        }
    }
}
