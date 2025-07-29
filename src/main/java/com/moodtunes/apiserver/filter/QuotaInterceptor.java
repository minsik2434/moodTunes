package com.moodtunes.apiserver.filter;

import com.moodtunes.apiserver.entity.ApiKey;
import com.moodtunes.apiserver.repository.ApiKeyRepository;
import com.moodtunes.apiserver.service.RedisService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
@Slf4j
public class QuotaInterceptor implements HandlerInterceptor {
    private final RedisService redisService;
    private final ApiKeyRepository apiKeyRepository;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String apiKeyString = request.getHeader("X-API-KEY");
        ApiKey apiKey = apiKeyRepository.findByApiKey(apiKeyString).get();
        String quotaString = redisService.getValue(apiKeyString).orElse(null);
        if (quotaString == null){
            redisService.incrementWithMidnightExpire(apiKeyString);
        } else {
            int currentQuota = Integer.parseInt(quotaString);
            if(apiKey.getQuotaLimit() <= currentQuota){
                HttpResponseUtil.sendErrorResponse(response, HttpStatus.TOO_MANY_REQUESTS, "QUOTA_EXCEEDED", "quota exceed");
                return false;
            }
            redisService.increment(apiKeyString);
        }

        return true;
    }
}
