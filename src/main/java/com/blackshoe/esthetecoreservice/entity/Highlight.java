package com.blackshoe.esthetecoreservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "highlights")
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Getter
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Highlight {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "highlight_id")
    private Long id;

    @Column(columnDefinition = "BINARY(16)", name = "highlight_uuid")
    private UUID highlightId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "photographer_id", foreignKey = @ForeignKey(name = "highlight_fk_photographer_id"))
    private User photographer;

    @CreatedDate
    private LocalDateTime createdAt;

    public void setPhotographer(User photographer) {
        this.photographer = photographer;
    }

    @PrePersist
    public void setHighlightId() {
        if (highlightId == null) {
            highlightId = UUID.randomUUID();
        }
    }
}
