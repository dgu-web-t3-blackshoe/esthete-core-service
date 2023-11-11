package com.blackshoe.esthetecoreservice.entity;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
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
@Builder
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Genre {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "genre_id")
    private Long id;

    @Column(columnDefinition = "BINARY(16)", name = "genre_uuid")
    private UUID genreId;

}
