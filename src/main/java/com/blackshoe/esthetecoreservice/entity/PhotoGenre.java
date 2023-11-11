package com.blackshoe.esthetecoreservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Entity
@Table(name = "photo_genres")
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Getter
@Builder
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class PhotoGenre {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "photo_genre_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "photo_id", foreignKey = @ForeignKey(name = "photo_genre_fk_photo_id"))
    private Photo photo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "genre_id", foreignKey = @ForeignKey(name = "photo_genre_fk_genre_id"))
    private Genre genre;
}
