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
@Table(name = "exhibitions")
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Getter
@EntityListeners(AuditingEntityListener.class)
public class Exhibition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "exhibition_id")
    private Long id;

    @Column(columnDefinition = "BINARY(16)", name = "exhibition_uuid")
    private UUID exhibitionId;

    @Column(name = "title", nullable = false, length = 50)
    private String title;

    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(name = "thumbnail", nullable = false, length = 100)
    private String thumbnail;

    @CreatedDate
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "exhibition", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Room> rooms;

    @Builder
    public Exhibition(String title, String description, String thumbnail) {
        this.title = title;
        this.description = description;
        this.thumbnail = thumbnail;
        this.rooms = new ArrayList<>();
    }

    @PrePersist
    public void setExhibitionId() {
        if (exhibitionId == null) {
            exhibitionId = UUID.randomUUID();
        }
    }

    public void addRoom(Room room) {
        rooms.add(room);
    }
}
