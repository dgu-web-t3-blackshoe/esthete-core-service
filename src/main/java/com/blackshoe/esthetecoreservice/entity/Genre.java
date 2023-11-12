package com.blackshoe.esthetecoreservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "genres")
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Getter
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Genre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "genre_id")
    private Long id;

    @Column(columnDefinition = "BINARY(16)", name = "genre_uuid")
    private UUID genreId;

    @Column(name = "genre_name")
    private String genreName;

    @PrePersist
    public void setGenreId() {
        if (genreId == null) {
            genreId = UUID.randomUUID();
        }
    }

    @Builder
    public Genre(String genreName) {
        this.genreName = genreName;
    }
}
