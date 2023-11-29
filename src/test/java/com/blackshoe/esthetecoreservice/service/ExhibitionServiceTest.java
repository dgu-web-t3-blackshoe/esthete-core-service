package com.blackshoe.esthetecoreservice.service;

import com.blackshoe.esthetecoreservice.dto.ExhibitionDto;
import com.blackshoe.esthetecoreservice.entity.Exhibition;
import com.blackshoe.esthetecoreservice.entity.ProfileImgUrl;
import com.blackshoe.esthetecoreservice.entity.User;
import com.blackshoe.esthetecoreservice.repository.ExhibitionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ExhibitionServiceTest {

    @InjectMocks
    private ExhibitionServiceImpl exhibitionService;

    @Mock
    private ExhibitionRepository exhibitionRepository;

    @Mock
    private final Exhibition exhibition = Exhibition.builder()
            .title("test")
            .description("test")
            .thumbnail("thumbnail")
            .build();

    @Spy
    private final User user = User.builder()
            .nickname("nickname")
            .biography("biography")
            .build();

    @Mock
    private final ProfileImgUrl profileImgUrl = ProfileImgUrl.builder()
            .cloudfrontUrl("cloudfrontUrl")
            .s3Url("s3Url")
            .build();

    private final UUID exhibitionId = UUID.randomUUID();

    private final UUID userId = UUID.randomUUID();

    private final LocalDateTime createdAt = LocalDateTime.now();

    private final ExhibitionDto.CreateExhibitionRequest exhibitionCreateRequest = ExhibitionDto.CreateExhibitionRequest.builder()
            .title("test")
            .description("test")
            .thumbnail("thumbnail")
            .build();

    @Test
    public void createExhibition_whenSuccess_returnsExhibitionCreateResponse() {
        // given
        when(exhibitionRepository.save(any(Exhibition.class))).thenReturn(exhibition);
        when(exhibition.getExhibitionId()).thenReturn(exhibitionId);
        when(exhibition.getCreatedAt()).thenReturn(createdAt);

        // when
        final ExhibitionDto.CreateExhibitionResponse exhibitionCreateExhibitionResponse = exhibitionService.createExhibition(exhibitionCreateRequest);

        // then
        verify(exhibitionRepository, times(1)).save(any(Exhibition.class));
        assertThat(exhibitionCreateExhibitionResponse.getExhibitionId()).isEqualTo(exhibitionId.toString());
        assertThat(exhibitionCreateExhibitionResponse.getCreatedAt()).isEqualTo(createdAt.toString());
    }

    @Test
    public void deleteExhibition_whenSuccess_returnsExhibitionDeleteResponse() {
        // given
        when(exhibitionRepository.findByExhibitionId(any(UUID.class))).thenReturn(Optional.of(exhibition));
        when(exhibition.getExhibitionId()).thenReturn(exhibitionId);

        // when
        final ExhibitionDto.DeleteExhibitionResponse exhibitionDeleteExhibitionResponse = exhibitionService.deleteExhibition(exhibitionId);

        // then
        verify(exhibitionRepository, times(1)).findByExhibitionId(any(UUID.class));
        verify(exhibitionRepository, times(1)).delete(any(Exhibition.class));
        assertThat(exhibitionDeleteExhibitionResponse.getExhibitionId()).isEqualTo(exhibitionId.toString());
        assertThat(exhibitionDeleteExhibitionResponse.getDeletedAt()).isNotNull();
    }

    @Test
    public void readRandomExhibition_whenSuccess_returnsExhibitionGetRandomResponse() {
        // given
        user.setProfileImgUrl(profileImgUrl);
        when(exhibitionRepository.findById(any(Long.class))).thenReturn(Optional.of(exhibition));
        when(exhibition.getExhibitionId()).thenReturn(exhibitionId);
        when(exhibition.getUser()).thenReturn(user);
        when(exhibition.getUser().getUserId()).thenReturn(userId);

        // when
        final ExhibitionDto.ReadRandomExhibitionResponse exhibitionReadRandomExhibitionResponse = exhibitionService.readRandomExhibition();

        // then
        verify(exhibitionRepository).findById(any(Long.class));
        assertThat(exhibitionReadRandomExhibitionResponse.getExhibitionId()).isEqualTo(exhibitionId.toString());
        assertThat(exhibitionReadRandomExhibitionResponse.getTitle()).isEqualTo(exhibition.getTitle());
        assertThat(exhibitionReadRandomExhibitionResponse.getDescription()).isEqualTo(exhibition.getDescription());
        assertThat(exhibitionReadRandomExhibitionResponse.getThumbnail()).isEqualTo(exhibition.getThumbnail());
        assertThat(exhibitionReadRandomExhibitionResponse.getUserId()).isEqualTo(userId.toString());
        assertThat(exhibitionReadRandomExhibitionResponse.getNickname()).isEqualTo(user.getNickname());
        assertThat(exhibitionReadRandomExhibitionResponse.getProfileImg()).isEqualTo(user.getProfileImgUrl().getCloudfrontUrl());
    }
}
