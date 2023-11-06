package com.blackshoe.esthetecoreservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "rooms")
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Getter
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    private Long id;

    @Column(columnDefinition = "BINARY(16)", name = "room_uuid")
    private UUID roomId;

    @Column(name = "title", nullable = false, length = 50)
    private String title;

    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(name = "thumbnail", nullable = false, length = 100)
    private String thumbnail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exhibition_id", foreignKey = @ForeignKey(name = "room_fk_exhibition_id"))
    private Exhibition exhibition;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RoomPhoto> roomPhotos;

    @CreatedDate
    private LocalDateTime createdAt;

    @PrePersist
    public void setRoomId() {
        if (roomId == null) {
            roomId = UUID.randomUUID();
        }
    }

    @Builder
    public Room(String title, String description, String thumbnail, Exhibition exhibition) {
        this.title = title;
        this.description = description;
        this.thumbnail = thumbnail;
        this.exhibition = exhibition;
        this.roomPhotos = new ArrayList<>();
    }

    public void setExhibition(Exhibition exhibition) {
        this.exhibition = exhibition;
    }

    public void addRoomPhoto(RoomPhoto roomPhoto) {
        this.roomPhotos.add(roomPhoto);
    }
}