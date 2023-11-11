package com.blackshoe.esthetecoreservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "photo_views")
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Getter
@Builder
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class PhotoView {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "photo_view_id")
    private Long id;

    //photoUUID
    @Column(columnDefinition = "BINARY(16)", name = "photo_uuid")
    private UUID photoId;

}
