package com.blackshoe.esthetecoreservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Entity
@Table(name = "photo_checksums")
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Getter
@Builder
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class PhotoChecksum {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "photo_checksum_id")
    private Long id;

    @Column(name = "checksum", nullable = false, length = 32)
    private String checksum;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "photo_id", foreignKey = @ForeignKey(name = "photo_checksum_fk_photo_id"))
    private Photo photo;
}
