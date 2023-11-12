package com.blackshoe.esthetecoreservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Getter
@Builder
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    private Long id;

    @Column(columnDefinition = "BINARY(16)", name = "user_uuid")
    private UUID userId;

    //nickname
    @Column(name = "nickname", nullable = false, length = 50)
    private String nickname;

    @Column(name = "biography", nullable = false, length = 1000)
    private String biography;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PhotoEquipment> userEquipments = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserGenre> userGenres = new ArrayList<>();

    //user profile img url
    @JoinColumn(name = "profile_img_url_id", foreignKey = @ForeignKey(name = "user_fk_profile_img_url_id"))
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private ProfileImgUrl profileImgUrl;

}
