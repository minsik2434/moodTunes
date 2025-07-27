package com.moodtunes.apiserver.service;

import com.moodtunes.apiserver.dto.ApiKeyDto;
import com.moodtunes.apiserver.dto.ApplicationInfoResponse;
import com.moodtunes.apiserver.dto.RegisterAppRequest;
import com.moodtunes.apiserver.dto.RegisterAppResponse;
import com.moodtunes.apiserver.entity.ApiKey;
import com.moodtunes.apiserver.entity.Application;
import com.moodtunes.apiserver.repository.ApiKeyRepository;
import com.moodtunes.apiserver.repository.ApplicationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ApplicationServiceUnitTest {

    @InjectMocks
    ApplicationService applicationService;
    @Mock
    ApplicationRepository applicationRepository;
    @Mock
    ApiKeyRepository apiKeyRepository;
    @Mock
    ApiKeyGenerator apiKeyGenerator;

    @Test
    void registerTest() throws NoSuchFieldException, IllegalAccessException {
        RegisterAppRequest request =
                new RegisterAppRequest("MyApp", "test@naver.com", 100);

        Application mockApplication = new Application(request.getAppName(), request.getOwnerEmail());
        ApiKey mockApiKey = new ApiKey(mockApplication, "generatedKey", request.getQuotaLimit(), true);
        Field id = mockApplication.getClass().getDeclaredField("id");
        Field createdAt = mockApplication.getClass().getDeclaredField("createdAt");
        Field issuedAt = mockApiKey.getClass().getDeclaredField("issuedAt");
        id.setAccessible(true);
        id.set(mockApplication, 1L);
        createdAt.setAccessible(true);
        createdAt.set(mockApplication, LocalDateTime.of(2025, 7, 25, 0, 0 ,0));
        issuedAt.setAccessible(true);
        issuedAt.set(mockApiKey, LocalDateTime.of(2025, 7, 25, 0, 0 ,0));

        when(applicationRepository.save(any(Application.class)))
                .thenReturn(mockApplication);
        when(apiKeyGenerator.generate()).thenReturn("generatedKey");
        when(apiKeyRepository.save(any(ApiKey.class)))
                .thenReturn(mockApiKey);


        RegisterAppResponse response = applicationService.register(request);

        assertThat(response.getAppId()).isEqualTo(1L);
        assertThat(response.getApiKey()).isEqualTo("generatedKey");
        assertThat(response.getIssuedAt()).isEqualTo(LocalDateTime.of(2025, 7, 25, 0, 0 ,0));
        assertThat(response.getQuotaLimit()).isEqualTo(100);
    }

    @Test
    void getInfoTest(){
        ApplicationInfoResponse response = applicationService.getInfo(1L);

        assertThat(response.getAppId())
                .isEqualTo(1L);
        assertThat(response.getName())
                .isEqualTo("MyApp");
        assertThat(response.getOwnerEmail())
                .isEqualTo("test@naver.com");

        assertThat(response.getApiKeys())
                .hasSize(2)
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
                        tuple(2L, "def2", 100, 55, false,
                                LocalDateTime.of(2025, 11, 24, 0,0,0));
                );
    }
}