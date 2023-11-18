package com.blackshoe.esthetecoreservice.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "new_works")
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Getter @Builder(toBuilder = true)
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class NewWork {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "new_work_id")
    private Long id;

    @Column(columnDefinition = "BINARY(16)", name = "new_work_uuid")
    private UUID newWorkId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "photographer_id", foreignKey = @ForeignKey(name = "new_work_fk_photographer_id"))
    private User photographer;

    @Column(columnDefinition = "BINARY(16)", name = "photographer_uuid")
    private UUID photographerId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "photo_id", foreignKey = @ForeignKey(name = "new_work_fk_photo_id"))
    private Photo photo;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exhibition_id", foreignKey = @ForeignKey(name = "new_work_fk_exhibition_id"))
    private Exhibition exhibition;

    @Column(name = "created_at")
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @LastModifiedDate
    private LocalDateTime updatedAt;

    public void setPhoto(Photo photo) {
        this.photo = photo;
    }

    public void setExhibition(Exhibition exhibition) {
        this.exhibition = exhibition;
    }

    @PrePersist
    public void setNewWorkId() {
        if (newWorkId == null) {
            newWorkId = UUID.randomUUID();
        }
    }

}
