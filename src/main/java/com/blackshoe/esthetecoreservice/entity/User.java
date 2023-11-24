package com.blackshoe.esthetecoreservice.entity;

import com.blackshoe.esthetecoreservice.vo.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Getter @Builder
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(columnDefinition = "BINARY(16)", name = "user_uuid")
    private UUID userId;

    @Column(name = "email", nullable = false, length = 50)
    private String email;

    @Column(name = "role", length = 20)
    private Role role;

    @Column(name = "nickname", nullable = false, length = 50)
    private String nickname;

    @Column(name = "biography", nullable = false, columnDefinition = "TEXT")
    private String biography;

    @JoinColumn(name = "profile_img_url_id", foreignKey = @ForeignKey(name = "user_fk_profile_img_url_id"))
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private ProfileImgUrl profileImgUrl;

    @CreatedDate
    @Column(name = "created_at", length = 20)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", length = 20)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserGenre> userGenres;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserEquipment> userEquipments;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Exhibition> exhibitions;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Photo> photos;

    @OneToMany(mappedBy = "photographer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GuestBook> guestBooks;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Support> supports;

    private Long supportCount;

    private Long viewCount;

    @Builder
    public User(String nickname, String biography) {
        this.nickname = nickname;
        this.biography = biography;
        this.userGenres = new ArrayList<>();
        this.userEquipments = new ArrayList<>();
        this.exhibitions = new ArrayList<>();
        this.photos = new ArrayList<>();
        this.guestBooks = new ArrayList<>();
        this.supports = new ArrayList<>();
        this.supportCount = 0L;
        this.viewCount = 0L;
    }

    @PrePersist
    public void setUserId() {
        if (userId == null) {
            userId = UUID.randomUUID();
        }
    }

    public void setProfileImgUrl(ProfileImgUrl profileImgUrl) {
        this.profileImgUrl = profileImgUrl;
    }

    public void addUserEquipment(UserEquipment userEquipment) {
        this.userEquipments.add(userEquipment);
    }

    public void addUserGenre(UserGenre userGenre) {
        this.userGenres.add(userGenre);
    }

    public void addExhibition(Exhibition exhibition) {
        this.exhibitions.add(exhibition);
    }

    public void addPhoto(Photo photo) {
        this.photos.add(photo);
    }

    public void addGuestBook(GuestBook guestBook) {
        this.guestBooks.add(guestBook);
    }

    public void addSupport(Support support) {
        this.supports.add(support);
        this.supportCount++;
    }

    public void removeSupport(Support support) {
        this.supportCount--;
    }

}
