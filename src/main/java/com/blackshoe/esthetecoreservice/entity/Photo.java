package com.blackshoe.esthetecoreservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "photos")
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Getter @Builder @AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Photo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "photo_id")
    private long id;

    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)", name = "photo_uuid")
    private UUID photoId;

    @Column(name = "title", nullable = false, length = 50)
    private String title;

    @Column(name = "description", nullable = true, length = 100)
    private String description;

    @Column(name = "detail", nullable = true, length = 100)
    private String detail;

    @JoinColumn(name = "photo_url_id", foreignKey = @ForeignKey(name = "photo_fk_photo_url_id"))
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private PhotoUrl photoUrl;

    /*
    @JoinColumn(name = "photo_location_id", foreignKey = @ForeignKey(name = "photo_fk_photo_location_id"))
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private PhotoLocation photoLocation;
    */

    @ColumnDefault("0")
    @Column(name = "collections_count")
    private long collectionsCount;

    @ColumnDefault("0")
    @Column(name = "view_count")
    private long viewCount;

    @ColumnDefault("true")
    @Column(nullable = false)
    private boolean isPublic;

    @CreatedDate
    @Column(name = "created_at", length = 20)
    private LocalDateTime createdAt;

}
