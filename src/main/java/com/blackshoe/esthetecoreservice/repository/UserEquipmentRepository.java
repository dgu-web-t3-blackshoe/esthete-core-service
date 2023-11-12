package com.blackshoe.esthetecoreservice.repository;

import com.blackshoe.esthetecoreservice.entity.UserEquipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserEquipmentRepository extends JpaRepository<UserEquipment, Long> {
}
