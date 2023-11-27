package com.blackshoe.esthetecoreservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import com.blackshoe.esthetecoreservice.dto.ProfileImgUrlDto;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "profile_img_urls")
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Getter
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class ProfileImgUrl {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "profile_img_url_id")
    private Long id;

    @Column(columnDefinition = "BINARY(16)", name = "profile_img_url_uuid")
    private UUID profileImgUrlId;

    private String cloudfrontUrl;

    private String s3Url;

    @Builder
    public ProfileImgUrl(UUID profileImgUrlId, String cloudfrontUrl, String s3Url) {
        this.profileImgUrlId = profileImgUrlId;
        this.cloudfrontUrl = cloudfrontUrl;
        this.s3Url = s3Url;
    }

    @PrePersist
    public void setProfileImgUrlId() {
        if (profileImgUrlId == null) {
            profileImgUrlId = UUID.randomUUID();
        }
    }

    public static ProfileImgUrl convertProfileImgUrlDtoToEntity(ProfileImgUrlDto uploadedProfileImgUrl) {
        return ProfileImgUrl.builder()
                .s3Url(uploadedProfileImgUrl.getS3Url())
                .cloudfrontUrl(uploadedProfileImgUrl.getCloudfrontUrl())
                .build();
    }

}
