package com.moodtunes.apiserver.filter;

import com.moodtunes.apiserver.service.RedisService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Optional;

@Component
public class RateLimitInterceptor implements HandlerInterceptor {
    private final RedisService redisService;
    public RateLimitInterceptor(RedisService redisService) {
        this.redisService = redisService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String path = request.getRequestURI();
        if(path.startsWith("/music")){
            String apiKey = request.getHeader("X-API-KEY");
            

        }

        return true;
    }
}
