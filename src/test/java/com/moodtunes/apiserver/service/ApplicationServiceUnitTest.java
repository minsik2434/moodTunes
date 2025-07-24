package com.moodtunes.apiserver.service;

import com.moodtunes.apiserver.dto.RegisterAppRequest;
import com.moodtunes.apiserver.dto.RegisterAppResponse;
import com.moodtunes.apiserver.entity.Application;
import com.moodtunes.apiserver.repository.ApplicationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class ApplicationServiceUnitTest {

        @InjectMocks
        ApplicationService applicationService;

        @Mock
        ApplicationRepository applicationRepository;

        @BeforeEach
        void setUp(){
            MockitoAnnotations.openMocks(this);
        }

        @Test
        void applicationRegisterTest(){
            RegisterAppRequest requestDto = new RegisterAppRequest("appName", "test@naver.com", 100);

            RegisterAppResponse response = applicationService.register(requestDto);

            assertThat(response.getAppId()).isEqualTo(1L);
            assertThat(response.getQuotaLimit()).isEqualTo(100);
        }

}