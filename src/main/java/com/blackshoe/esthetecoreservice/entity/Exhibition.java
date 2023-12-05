package com.blackshoe.esthetecoreservice.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
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
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "exhibition_id")
    private Long id;

    @Column(columnDefinition = "BINARY(16)", name = "exhibition_uuid")
    private UUID exhibitionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "exhibition_fk_user_id"))
    private User user;

    @Column(name = "title", nullable = false, length = 50)
    private String title;

    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(name = "thumbnail", nullable = false, length = 100)
    private String thumbnail;

    @CreatedDate
    @Column(name = "created_at", nullable = false, length = 20)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "exhibition", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Room> rooms;

    @ColumnDefault("0")
    @Column(name = "view_count")
    private Long viewCount;

    @Column(name = "exhibition_genres")
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExhibitionGenre> exhibitionGenres = new ArrayList<>();

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

    public void setUser(User user) {
        this.user = user;
        user.addExhibition(this);
    }

    public void addRoom(Room room) {
        rooms.add(room);
    }

    public void addExhibitionGenre(ExhibitionGenre exhibitionGenre) {
        exhibitionGenres.add(exhibitionGenre);
    }

    public void increaseViewCount() {
        this.viewCount++;
    }

}
