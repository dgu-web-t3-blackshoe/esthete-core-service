package com.blackshoe.esthetecoreservice.entity;

import com.blackshoe.esthetecoreservice.dto.PhotoUrlDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "photo_urls")
@NoArgsConstructor
@Getter
public class PhotoUrl {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "photo_url_id")
    private long id;

    @Column(columnDefinition = "BINARY(16)", name = "photo_url_uuid")
    private UUID photoUrlId;

    @Column(name = "s3_url")
    private String s3Url;

    @Column(name = "cloudfront_url")
    private String cloudfrontUrl;

    @Builder
    public PhotoUrl(long id, UUID photoUrlId, String s3Url, String cloudfrontUrl) {
        this.id = id;
        this.photoUrlId = photoUrlId;
        this.s3Url = s3Url;
        this.cloudfrontUrl = cloudfrontUrl;
    }

    public static PhotoUrl convertPhotoUrlDtoToEntity(PhotoUrlDto uploadedPhotoUrlDto) {
        return PhotoUrl.builder()
                .s3Url(uploadedPhotoUrlDto.getS3Url())
                .cloudfrontUrl(uploadedPhotoUrlDto.getCloudfrontUrl())
                .build();
    }

    @PrePersist
    public void setPhotoUrlId() {
        if (photoUrlId == null) {
            photoUrlId = UUID.randomUUID();
        }
    }
}
