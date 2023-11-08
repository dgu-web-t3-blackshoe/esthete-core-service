package com.blackshoe.esthetecoreservice.entity;

import com.blackshoe.esthetecoreservice.dto.PhotoUrlDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "photo_urls")
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Getter
@Builder
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
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
