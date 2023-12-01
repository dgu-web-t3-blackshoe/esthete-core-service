package com.blackshoe.esthetecoreservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Entity
@Table(name = "exhibition_genres")
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Getter
@Builder
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class ExhibitionGenre {
    @Id
    @GeneratedValue
    @Column(name = "exhibition_genre_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exhibition_id", foreignKey = @ForeignKey(name = "exhibition_genre_fk_exhibition_id"))
    private Exhibition exhibition;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "genre_id", foreignKey = @ForeignKey(name = "exhibition_genre_fk_genre_id"))
    private Genre genre;

    @Builder
    public ExhibitionGenre(Genre genre) {
        this.genre = genre;
    }

    public void setExhibition(Exhibition exhibition) {
        this.exhibition = exhibition;
        exhibition.addExhibitionGenre(this);
    }
}
