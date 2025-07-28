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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        List<ApiKeyDto> apiKeyDtoList = new ArrayList<>();
        for (ApiKey apiKey : application.getApiKeys()) {
            String value = redisService.getValue(apiKey.getApiKey()).orElse(null);
            int remainingQuota = getRemainingQuota(value, apiKey.getQuotaLimit());
            ApiKeyDto apiKeyDto = new ApiKeyDto(apiKey.getId(),
                    getKeyPrefix(apiKey.getApiKey()),
                    apiKey.getQuotaLimit(),
                    remainingQuota,
                    apiKey.isActivate(),
                    apiKey.getIssuedAt()
            );

            apiKeyDtoList.add(apiKeyDto);
        }

        return new ApplicationInfoResponse(
                application.getId(),
                application.getName(),
                application.getOwnerEmail(),
                apiKeyDtoList
        );
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
}
