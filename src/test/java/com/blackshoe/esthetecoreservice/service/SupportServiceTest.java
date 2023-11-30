package com.blackshoe.esthetecoreservice.service;

import com.blackshoe.esthetecoreservice.dto.SupportDto;
import com.blackshoe.esthetecoreservice.entity.Support;
import com.blackshoe.esthetecoreservice.entity.User;
import com.blackshoe.esthetecoreservice.repository.SupportRepository;
import com.blackshoe.esthetecoreservice.repository.UserRepository;
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
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SupportServiceTest {

    @InjectMocks
    private SupportServiceImpl supportService;

    @Mock
    private SupportRepository supportRepository;

    @Mock
    private UserRepository userRepository;

    private final User photographer = User.builder()
            .nickname("photographer")
            .biography("biography")
            .build();

    private final User user = User.builder()
            .nickname("user")
            .biography("biography")
            .build();

    private final UUID photographerId = UUID.randomUUID();

    private final UUID userId = UUID.randomUUID();

    @Spy
    private final Support support = Support.builder()
            .photographer(photographer)
            .build();

    @Test
    public void createSupport_whenSuccess_returnsSupportCreateResponse() {
        // given
        final SupportDto.CreateSupportRequest supportCreateSupportRequest = SupportDto.CreateSupportRequest.builder()
                .photographerId(photographerId.toString())
                .build();

        support.setUser(user);

        when(userRepository.findByUserId(photographerId)).thenReturn(Optional.of(photographer));
        when(userRepository.findByUserId(userId)).thenReturn(Optional.of(user));
        when(supportRepository.save(any(Support.class))).thenReturn(support);
        when(support.getSupportId()).thenReturn(UUID.randomUUID());
        when(support.getCreatedAt()).thenReturn(LocalDateTime.now());

        // when
        final SupportDto.CreateSupportResponse supportCreateSupportResponse = supportService.createSupport(userId, supportCreateSupportRequest);

        // then
        assertThat(supportCreateSupportResponse).isNotNull();
        assertThat(supportCreateSupportResponse.getSupportId()).isNotNull();
        assertThat(supportCreateSupportResponse.getCreatedAt()).isNotNull();
    }

    @Test
    public void deleteSupport_whenSuccess_returnsSupportDeleteResponse() {
        // given
        when(supportRepository.findByUserIdAndPhotographerId(userId, photographerId)).thenReturn(Optional.of(support));
        when(support.getSupportId()).thenReturn(UUID.randomUUID());
        doNothing().when(support).unsetUser();

        // when
        final SupportDto.DeleteSupportResponse supportDeleteSupportResponse = supportService.deleteSupport(userId, photographerId);

        // then
        assertThat(supportDeleteSupportResponse).isNotNull();
        assertThat(supportDeleteSupportResponse.getSupportId()).isNotNull();
        assertThat(supportDeleteSupportResponse.getDeletedAt()).isNotNull();
    }
}
