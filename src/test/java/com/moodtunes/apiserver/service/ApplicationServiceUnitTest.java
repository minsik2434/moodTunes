package com.moodtunes.apiserver.service;

import com.moodtunes.apiserver.dto.RegisterAppRequest;
import com.moodtunes.apiserver.dto.RegisterAppResponse;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ApplicationServiceUnitTest {

        @InjectMocks
        ApplicationService applicationService;

        @Test
        void applicationRegisterTest(){
            RegisterAppRequest requestDto = new RegisterAppRequest("appName", "test@naver.com", 100);

            RegisterAppResponse response = applicationService.register(requestDto);

            assertThat(response.getAppId()).isEqualTo(1L);
            assertThat(response.getApiKey()).isEqualTo("apiKey");
            assertThat(response.getQuotaLimit()).isEqualTo(100);
        }

}