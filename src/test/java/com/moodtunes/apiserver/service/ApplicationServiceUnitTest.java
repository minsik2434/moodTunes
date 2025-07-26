package com.moodtunes.apiserver.service;

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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
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
        id.setAccessible(true);
        id.set(mockApplication, 1L);
        createdAt.setAccessible(true);
        createdAt.set(mockApplication, LocalDateTime.of(2025, 7, 25, 0, 0 ,0));

        when(applicationRepository.save(any(Application.class)))
                .thenReturn(mockApplication);
        when(apiKeyGenerator.generate()).thenReturn("generatedKey");
        when(apiKeyRepository.save(any(ApiKey.class)))
                .thenReturn(mockApiKey);


        RegisterAppResponse response = applicationService.register(request);

        assertThat(response.getAppId()).isEqualTo(1L);
        assertThat(response.getApiKey()).isEqualTo("generatedKey");
        assertThat(response.getQuotaLimit()).isEqualTo(100);
    }
}