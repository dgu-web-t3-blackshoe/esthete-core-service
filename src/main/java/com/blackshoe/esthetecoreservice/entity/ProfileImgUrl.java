package com.blackshoe.esthetecoreservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "profile_img_urls")
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Getter
@Builder
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class ProfileImgUrl {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "profile_img_url_id")
    private long id;

    @Column(columnDefinition = "BINARY(16)", name = "profile_img_url_uuid")
    private UUID profileImgUrlId;

    @Column(name = "s3_url")
    private String s3Url;

    @Column(name = "cloudfront_url")
    private String cloudfrontUrl;

    /*
    public static PhotoUrl convertUserUrlDtoToEntity(PhotoUrlDto uploadedPhotoUrlDto) {
        return PhotoUrl.builder()
                .s3Url(uploadedPhotoUrlDto.getS3Url())
                .cloudfrontUrl(uploadedPhotoUrlDto.getCloudfrontUrl())
                .build();
    }
*/
    @PrePersist
    public void setProfileImgUrlId() {
        if (profileImgUrlId == null) {
            profileImgUrlId = UUID.randomUUID();
        }
    }
}
