package com.moodtunes.apiserver.filter;

import com.moodtunes.apiserver.entity.ApiKey;
import com.moodtunes.apiserver.repository.ApiKeyRepository;
import com.moodtunes.apiserver.service.RedisService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
        ApiKey apiKey = apiKeyRepository.findByApiKey(apiKeyString).orElse(null);
        // 로직 추가해야함
        if(apiKey == null){

        }
        Long increment = redisService.increment(apiKeyString);
        log.info("{}", increment);

        if(apiKey.getQuotaLimit() < increment){
            return false;
        }
        return true;
    }
}
