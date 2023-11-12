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
@Table(name = "guest_books")
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Getter
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class GuestBook {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "guest_book_id")
    private Long id;

    @Column(columnDefinition = "BINARY(16)", name = "user_equipment_uuid")
    private UUID guestBookId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "photographer_id", foreignKey = @ForeignKey(name = "guest_book_fk_photographer_id"))
    private User photographer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "guest_book_fk_user_id"))
    private User user;

    @Column(name = "guest_book_content")
    private String content;

    @CreatedDate
    private LocalDateTime createdAt;

    @Builder
    public GuestBook(User user, String content) {
        this.user = user;
        this.content = content;
    }

    public void setPhotographer(User photographer) {
        this.photographer = photographer;
        photographer.addGuestBook(this);
    }

    @PrePersist
    public void setGuestBookId() {
        if (guestBookId == null) {
            guestBookId = UUID.randomUUID();
        }
    }
}
