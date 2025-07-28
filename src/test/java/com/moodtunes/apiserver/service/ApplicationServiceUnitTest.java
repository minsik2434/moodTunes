package com.moodtunes.apiserver.service;

import com.moodtunes.apiserver.dto.ApiKeyDto;
import com.moodtunes.apiserver.dto.ApplicationInfoResponse;
import com.moodtunes.apiserver.dto.RegisterAppRequest;
import com.moodtunes.apiserver.dto.RegisterAppResponse;
import com.moodtunes.apiserver.entity.ApiKey;
import com.moodtunes.apiserver.entity.Application;
import com.moodtunes.apiserver.exception.NotFoundException;
import com.moodtunes.apiserver.repository.ApplicationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApplicationServiceUnitTest {

    @InjectMocks
    ApplicationService applicationService;
    @Mock
    ApplicationRepository applicationRepository;
    @Mock
    ApiKeyGenerator apiKeyGenerator;
    @Mock
    RedisService redisService;

    @Test
    void registerTest() throws NoSuchFieldException, IllegalAccessException {

        when(apiKeyGenerator.generate()).thenReturn("generatedKey");
        when(applicationRepository.save(any(Application.class)))
                .thenAnswer(invocation -> {
                    Application app = invocation.getArgument(0);
                    ReflectionTestUtils.setField(app, "id", 1L);
                    ApiKey apiKey = app.getApiKeys().get(0);
                    ReflectionTestUtils.setField(apiKey, "issuedAt",
                            LocalDateTime.of(2025, 7, 27, 12, 0, 0));

                    return app;
                });

        RegisterAppRequest request = new RegisterAppRequest("MyApp", "test@naver.com", 100);
        RegisterAppResponse response = applicationService.register(request);

        assertThat(response.getAppId()).isEqualTo(1L);
        assertThat(response.getApiKey()).isEqualTo("generatedKey");
        assertThat(response.getQuotaLimit()).isEqualTo(100);
        assertThat(response.getIssuedAt()).isEqualTo(LocalDateTime.of(
                2025, 7, 27, 12, 0, 0
        ));
    }

    @Test
    void getInfoTest(){

        Application application = new Application("MyApp", "test@naver.com");
        ReflectionTestUtils.setField(application, "id", 1L);
        ApiKey key1 = application.addApiKey("abc1fasdfv", 100, true);
        ReflectionTestUtils.setField(key1, "id", 1L);
        ReflectionTestUtils.setField(key1, "issuedAt", LocalDateTime.of(2025,12,25, 0,0,0));

        ApiKey key2 = application.addApiKey("def2asdvczx", 100, false);
        ReflectionTestUtils.setField(key2, "id", 2L);
        ReflectionTestUtils.setField(key2, "issuedAt", LocalDateTime.of(2025, 11, 24, 0,0,0));

        ApiKey key3 = application.addApiKey("ghi323dcczxv", 100, true);
        ReflectionTestUtils.setField(key3, "id", 3L);
        ReflectionTestUtils.setField(key3, "issuedAt", LocalDateTime.of(2025, 7, 25, 0, 0, 0));
        when(applicationRepository.findById(1L))
                .thenReturn(Optional.of(application));

        when(redisService.getValue("abc1fasdfv")).thenReturn(Optional.of("45"));
        when(redisService.getValue("def2asdvczx")).thenReturn(Optional.of("55"));
        when(redisService.getValue("ghi323dcczxv")).thenReturn(Optional.empty());

        ApplicationInfoResponse response = applicationService.getInfo(1L);

        assertThat(response.getAppId())
                .isEqualTo(1L);
        assertThat(response.getName())
                .isEqualTo("MyApp");
        assertThat(response.getOwnerEmail())
                .isEqualTo("test@naver.com");

        assertThat(response.getApiKeys())
                .hasSize(3)
                .extracting(
                        ApiKeyDto::getId,
                        ApiKeyDto::getKeyPrefix,
                        ApiKeyDto::getQuotaLimit,
                        ApiKeyDto::getRemainingQuota,
                        ApiKeyDto::isActivate,
                        ApiKeyDto::getIssuedAt
                )
                .containsExactlyInAnyOrder(
                        tuple(1L, "abc1", 100, 55, true,
                                LocalDateTime.of(2025,12,25, 0,0,0)),
                        tuple(2L, "def2", 100, 45, false,
                                LocalDateTime.of(2025, 11, 24, 0,0,0)),
                        tuple(3L, "ghi3", 100, 100, true,
                                LocalDateTime.of(2025, 7, 25, 0, 0, 0))
                );
    }

    @Test
    void getInfoTest_notFound(){
        when(applicationRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> applicationService.getInfo(1L))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("NotFound Application");
    }

    @Test
    void deleteTest(){
        Application application = new Application("MyApp", "test@naver.com");
        ReflectionTestUtils.setField(application, "id", 1L);
        ApiKey key1 = application.addApiKey("abc1fasdfv", 100, true);
        ReflectionTestUtils.setField(key1, "id", 1L);
        ReflectionTestUtils.setField(key1, "issuedAt", LocalDateTime.of(2025,12,25, 0,0,0));

        ApiKey key2 = application.addApiKey("def2asdvczx", 100, false);
        ReflectionTestUtils.setField(key2, "id", 2L);
        ReflectionTestUtils.setField(key2, "issuedAt", LocalDateTime.of(2025, 11, 24, 0,0,0));

        when(applicationRepository.findById(1L))
                .thenReturn(Optional.of(application));

        applicationService.delete(1L);

        verify(redisService, times(2)).deleteValue(anyString());
        verify(applicationRepository).delete(application);
    }

    @Test
    void deleteTest_notFound(){
        when(applicationRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> applicationService.delete(1L))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("NotFound Application");
    }
}