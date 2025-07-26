package com.moodtunes.apiserver.service;

import com.moodtunes.apiserver.dto.RegisterAppRequest;
import com.moodtunes.apiserver.dto.RegisterAppResponse;
import com.moodtunes.apiserver.entity.ApiKey;
import com.moodtunes.apiserver.entity.Application;
import com.moodtunes.apiserver.repository.ApiKeyRepository;
import com.moodtunes.apiserver.repository.ApplicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final ApiKeyRepository apiKeyRepository;

    @Transactional
    public RegisterAppResponse register(RegisterAppRequest request){
        Application application = applicationRepository.save(new Application(request.getAppName(), request.getOwnerEmail()));
        ApiKey apiKey = apiKeyRepository.save(new ApiKey(application, "apiKey", request.getQuotaLimit(), true));
        return new RegisterAppResponse(application.getId(), apiKey.getApiKey(), apiKey.getQuotaLimit(), apiKey.getIssuedAt());
    }
}
