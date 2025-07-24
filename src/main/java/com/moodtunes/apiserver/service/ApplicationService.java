package com.moodtunes.apiserver.service;

import com.moodtunes.apiserver.dto.RegisterAppRequest;
import com.moodtunes.apiserver.dto.RegisterAppResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ApplicationService {

    public RegisterAppResponse register(RegisterAppRequest request){
        return RegisterAppResponse
                .builder()
                .appId(1L)
                .apiKey("apiKey")
                .quotaLimit(100)
                .issuedAt(LocalDateTime.now())
                .build();

    }
}
