package com.moodtunes.apiserver.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moodtunes.apiserver.dto.RegisterAppRequest;
import com.moodtunes.apiserver.dto.RegisterAppResponse;
import com.moodtunes.apiserver.service.ApplicationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ApplicationController.class)
class ApplicationControllerTest {

    @Autowired
    MockMvc mockMvc;
    @MockitoBean
    ApplicationService applicationService;

    ObjectMapper mapper = new ObjectMapper();

    @Test
    void registerApplicationTest() throws Exception {
        RegisterAppRequest request = new RegisterAppRequest("appName", "email@naver.com", 100);
        RegisterAppResponse response = new RegisterAppResponse(1L, "abcdef", 100, LocalDateTime.of(2025, 7,
                28, 0, 0, 0));

        when(applicationService.register(any(RegisterAppRequest.class)))
                .thenReturn(response);

        String requestBody = mapper.writeValueAsString(request);
        ResultActions perform = mockMvc.perform(post("/apps")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        perform.andExpect(status().isCreated())
                .andExpect(jsonPath("$.appId").value(response.getAppId()))
                .andExpect(jsonPath("$.apiKey").value(response.getApiKey()))
                .andExpect(jsonPath("$.quotaLimit").value(response.getQuotaLimit()))
                .andExpect(jsonPath("$.issuedAt").value(response.getIssuedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)));
    }

    @ParameterizedTest
    @MethodSource("providedInvalidRegisterRequest")
    void registerApplication_badRequest(RegisterAppRequest request, String error, String message) throws Exception {
        String requestBody = mapper.writeValueAsString(request);

        ResultActions perform = mockMvc.perform(post("/apps")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        perform
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value(error))
                .andExpect(jsonPath("$.message").value(message));
    }

    private static Stream<Arguments> providedInvalidRegisterRequest(){
        return Stream.of(
                Arguments.of(
                        new RegisterAppRequest("", "test@naver.com", 100),
                        "Bad Request",
                        "appName: appName is required"
                ),
                Arguments.of(new RegisterAppRequest("myApp", "", 100),
                        "Bad Request",
                        "ownerEmail: ownerEmail is required"
                ),
                Arguments.of(new RegisterAppRequest("myApp", "test123", 100),
                        "Bad Request",
                        "ownerEmail: ownerEmail is not in format"
                ),
                Arguments.of(new RegisterAppRequest("myApp", "test@naver.com", 0),
                        "Bad Request",
                        "quotaLimit: quotaLimit must be greater than or equal to 1"
                )
        );
    }
}