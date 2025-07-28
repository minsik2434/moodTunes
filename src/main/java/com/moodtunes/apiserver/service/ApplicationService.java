package com.moodtunes.apiserver.service;

import com.moodtunes.apiserver.dto.ApiKeyDto;
import com.moodtunes.apiserver.dto.ApplicationInfoResponse;
import com.moodtunes.apiserver.dto.RegisterAppRequest;
import com.moodtunes.apiserver.dto.RegisterAppResponse;
import com.moodtunes.apiserver.entity.ApiKey;
import com.moodtunes.apiserver.entity.Application;
import com.moodtunes.apiserver.exception.NotFoundException;
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
    private final RedisService redisService;

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
                .orElseThrow(() -> new NotFoundException("NotFound Application"));
        List<ApiKeyDto> apiKeyDtoList = application.getApiKeys().stream().map(this::mapToDto).toList();

        return new ApplicationInfoResponse(
                application.getId(),
                application.getName(),
                application.getOwnerEmail(),
                apiKeyDtoList
        );
    }

    @Transactional
    public void delete(Long appId){
        Application application = applicationRepository.findById(appId)
                .orElseThrow(() -> new NotFoundException("NotFound Application"));

        for (ApiKey apiKey : application.getApiKeys()) {
            redisService.deleteValue(apiKey.getApiKey());
        }
        applicationRepository.delete(application);
    }

    private String getKeyPrefix(String apiKey){
        return apiKey.substring(0, 4);
    }

    private int getRemainingQuota(String currentQuota, int limitQuota){
        if(currentQuota == null || currentQuota.isEmpty()){
            return limitQuota;
        }
        return limitQuota - Integer.parseInt(currentQuota);
    }

    private ApiKeyDto mapToDto(ApiKey apiKey){
        String quota = redisService.getValue(apiKey.getApiKey()).orElse(null);
        int remainingQuota = getRemainingQuota(quota, apiKey.getQuotaLimit());
        return new ApiKeyDto(apiKey.getId(),
                getKeyPrefix(apiKey.getApiKey()),
                apiKey.getQuotaLimit(),
                remainingQuota,
                apiKey.isActivate(),
                apiKey.getIssuedAt());
    }
}
