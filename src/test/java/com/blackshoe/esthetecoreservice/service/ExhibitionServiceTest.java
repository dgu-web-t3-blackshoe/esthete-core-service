package com.blackshoe.esthetecoreservice.service;

import com.blackshoe.esthetecoreservice.dto.ExhibitionDto;
import com.blackshoe.esthetecoreservice.entity.Exhibition;
import com.blackshoe.esthetecoreservice.repository.ExhibitionRepository;
import com.blackshoe.esthetecoreservice.repository.RoomRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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

    private final UUID exhibitionId = UUID.randomUUID();

    private final LocalDateTime createdAt = LocalDateTime.now();

    private final ExhibitionDto.CreateRequest exhibitionCreateRequest = ExhibitionDto.CreateRequest.builder()
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
        final ExhibitionDto.CreateResponse exhibitionCreateResponse = exhibitionService.createExhibition(exhibitionCreateRequest);

        // then
        verify(exhibitionRepository, times(1)).save(any(Exhibition.class));
        assertThat(exhibitionCreateResponse.getExhibitionId()).isEqualTo(exhibitionId.toString());
        assertThat(exhibitionCreateResponse.getCreatedAt()).isEqualTo(createdAt.toString());
    }

    @Test
    public void deleteExhibition_whenSuccess_returnsExhibitionDeleteResponse() {
        // given
        when(exhibitionRepository.findByExhibitionId(any(UUID.class))).thenReturn(Optional.of(exhibition));
        when(exhibition.getExhibitionId()).thenReturn(exhibitionId);

        // when
        final ExhibitionDto.DeleteResponse exhibitionDeleteResponse = exhibitionService.deleteExhibition(exhibitionId);

        // then
        verify(exhibitionRepository, times(1)).findByExhibitionId(any(UUID.class));
        verify(exhibitionRepository, times(1)).delete(any(Exhibition.class));
        assertThat(exhibitionDeleteResponse.getExhibitionId()).isEqualTo(exhibitionId.toString());
        assertThat(exhibitionDeleteResponse.getDeletedAt()).isNotNull();
    }
}
