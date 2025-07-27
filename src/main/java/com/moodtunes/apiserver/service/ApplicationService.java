package com.moodtunes.apiserver.service;

import com.moodtunes.apiserver.dto.ApiKeyDto;
import com.moodtunes.apiserver.dto.ApplicationInfoResponse;
import com.moodtunes.apiserver.dto.RegisterAppRequest;
import com.moodtunes.apiserver.dto.RegisterAppResponse;
import com.moodtunes.apiserver.entity.ApiKey;
import com.moodtunes.apiserver.entity.Application;
import com.moodtunes.apiserver.exception.NotFoundException;
import com.moodtunes.apiserver.repository.ApiKeyRepository;
import com.moodtunes.apiserver.repository.ApplicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final ApiKeyGenerator apiKeyGenerator;

    @Transactional
    public RegisterAppResponse register(RegisterAppRequest request){
        Application application = new Application(request.getAppName(), request.getOwnerEmail());
        String keyString = apiKeyGenerator.generate();
        ApiKey apiKey = application.addApiKey(keyString, request.getQuotaLimit(), true);
        Application savedApplication = applicationRepository.save(application);

        return new RegisterAppResponse(
                savedApplication.getId(),
                apiKey.getApiKey(),
                apiKey.getQuotaLimit(),
                apiKey.getIssuedAt()
        );
    }

    @Transactional(readOnly = true)
    public ApplicationInfoResponse getInfo(Long appId){
        Application application = applicationRepository.findById(appId)
                .orElseThrow(() -> new NotFoundException("NotFound"));
        List<ApiKey> apiKeys = application.getApiKeys();
        //TODO Redis 에서 일일 호출가능 정보 조회 후 remaining에 설정해 반환해야함
        List<ApiKeyDto> list = apiKeys.stream().map(apiKey -> new ApiKeyDto(apiKey.getId(), getKeyPrefix(apiKey.getApiKey()),
                        apiKey.getQuotaLimit(), 55, apiKey.isActivate(), apiKey.getIssuedAt()))
                .toList();

        return new ApplicationInfoResponse(
                application.getId(),
                application.getName(),
                application.getOwnerEmail(),
                list
        );
    }

    private String getKeyPrefix(String apiKey){
        return apiKey.substring(0, 4);
    }
}
