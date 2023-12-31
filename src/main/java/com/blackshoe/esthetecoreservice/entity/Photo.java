package com.blackshoe.esthetecoreservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "photos")
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Getter @Builder(toBuilder = true) @AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Photo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "photo_id")
    private Long id;

    @Column(columnDefinition = "BINARY(16)", name = "photo_uuid")
    private UUID photoId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "photo_fk_user_id"))
    private User user;

    @Column(name = "title", nullable = false, length = 50)
    private String title;

    @Column(name = "description", nullable = true, length = 100)
    private String description;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "photo_url_id", foreignKey = @ForeignKey(name = "photo_fk_photo_url_id"))
    private PhotoUrl photoUrl;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "photo_checksum_id", foreignKey = @ForeignKey(name = "photo_checksum_fk_photo_id"))
    private PhotoChecksum photoChecksum;

    @Column(name = "photo_time", length = 20)
    private String time;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "photo_location_id", foreignKey = @ForeignKey(name = "photo_fk_photo_location_id"))
    private PhotoLocation photoLocation;

    //equipments
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PhotoEquipment> photoEquipments = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PhotoGenre> photoGenres = new ArrayList<>();

    @Column(name = "view_count")
    @ColumnDefault("0")
    private long viewCount;

    @CreatedDate
    @Column(name = "created_at", length = 20)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "photo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RoomPhoto> roomPhotos = new ArrayList<>();

    public void setPhotoUrl(PhotoUrl photoUrl) {
        this.photoUrl = photoUrl;
    }

    public void setPhotoChecksum(PhotoChecksum photoChecksum) {
        this.photoChecksum = photoChecksum;
    }

    public void setPhotoLocation(PhotoLocation photoLocation) {
        this.photoLocation = photoLocation;
    }

    public void setUser(User user) {
        this.user = user;
        user.addPhoto(this);
    }

    @PrePersist
    public void setPhotoIdAndPhotoGenres() {
        if (photoId == null) {
            photoId = UUID.randomUUID();
        }
    }

    public void addPhotoGenre(PhotoGenre photoGenre) {
        this.photoGenres.add(photoGenre);
    }

    public void addPhotoEquipment(PhotoEquipment photoEquipment) {
        this.photoEquipments.add(photoEquipment);
    }
    public void increaseViewCount() {
        this.viewCount++;
    }

    public void addRoomPhoto(RoomPhoto roomPhoto) {
        this.roomPhotos.add(roomPhoto);
    }
}
