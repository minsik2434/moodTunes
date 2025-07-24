package com.moodtunes.apiserver.service;

import com.moodtunes.apiserver.dto.RegisterAppRequest;
import com.moodtunes.apiserver.dto.RegisterAppResponse;
import com.moodtunes.apiserver.entity.Application;
import com.moodtunes.apiserver.repository.ApplicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final ApplicationRepository applicationRepository;

    public RegisterAppResponse register(RegisterAppRequest request){
        Application application = new Application(request.getAppName(), request.getOwnerEmail());
        String apiKey = KeyGenerator.generate();
        application.addApiKey(apiKey, request.getQuotaLimit(), true);
        applicationRepository.save(application);
        return new RegisterAppResponse(application.getId(), apiKey, 100, application.getCreateAt());
    }
}
