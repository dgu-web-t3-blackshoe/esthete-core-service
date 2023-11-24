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
@Table(name = "supports", uniqueConstraints = @UniqueConstraint(name = "support_uk", columnNames = {"photographer_id", "user_id"}))
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Getter
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Support {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "support_id")
    private Long id;

    @Column(columnDefinition = "BINARY(16)", name = "support_uuid")
    private UUID supportId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "photographer_id", foreignKey = @ForeignKey(name = "support_fk_photographer_id"))
    private User photographer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "support_fk_user_id"))
    private User user;

    @CreatedDate
    private LocalDateTime createdAt;

    @Builder
    public Support(User photographer) {
        this.photographer = photographer;
    }

    public void setUser(User user) {
        this.user = user;
        user.addSupport(this);
    }

    public void unsetUser() {
        this.user.removeSupport(this);
        this.user = null;
    }

    @PrePersist
    public void setSupportId() {
        if (supportId == null) {
            supportId = UUID.randomUUID();
        }
    }
}
