package com.moodtunes.apiserver.service;

import com.moodtunes.apiserver.dto.RegisterAppRequest;
import com.moodtunes.apiserver.dto.RegisterAppResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class ApplicationServiceUnitTest {

    @InjectMocks
    ApplicationService applicationService;

    @Test
    void registerTest(){
        RegisterAppRequest request =
                new RegisterAppRequest("MyApp", "test@naver.com", 100);

        RegisterAppResponse response = applicationService.register(request);

        assertThat(response.getAppId()).isEqualTo(1L);
        assertThat(response.getApiKey()).isEqualTo("apiKey");
        assertThat(response.getQuotaLimit()).isEqualTo(100);
    }

}