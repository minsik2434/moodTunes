package com.moodtunes.apiserver.service;

import com.moodtunes.apiserver.dto.ApiKeyDto;
import com.moodtunes.apiserver.dto.ApplicationInfoResponse;
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
import java.util.List;

@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final ApiKeyRepository apiKeyRepository;
    private final ApiKeyGenerator apiKeyGenerator;

    @Transactional
    public RegisterAppResponse register(RegisterAppRequest request){
        Application application = applicationRepository.save(new Application(request.getAppName(), request.getOwnerEmail()));
        String generatedApiKey = apiKeyGenerator.generate();
        ApiKey apiKey = apiKeyRepository.save(new ApiKey(application, generatedApiKey, request.getQuotaLimit(), true));
        return new RegisterAppResponse(application.getId(), apiKey.getApiKey(), apiKey.getQuotaLimit(), apiKey.getIssuedAt());
    }

    @Transactional(readOnly = true)
    public ApplicationInfoResponse getInfo(Long appId){
        return new ApplicationInfoResponse(
                1L,
                "MyApp",
                "test@naver.com",
                List.of(new ApiKeyDto(1L, "abc1", 100, 55, true,
                        LocalDateTime.of(2025,12,25, 0,0,0)),
                        new ApiKeyDto(2L, "def2", 100, 55, false,
                                LocalDateTime.of(2025, 11, 24, 0,0,0))
                )
        );
    }
}
