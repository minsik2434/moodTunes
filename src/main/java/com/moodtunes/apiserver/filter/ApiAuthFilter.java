package com.moodtunes.apiserver.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.moodtunes.apiserver.dto.ErrorResponse;
import com.moodtunes.apiserver.entity.ApiKey;
import com.moodtunes.apiserver.repository.ApiKeyRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
public class ApiAuthFilter extends OncePerRequestFilter {
    private final ApiKeyRepository apiKeyRepository;
    private ObjectMapper mapper = new ObjectMapper();
    public ApiAuthFilter(ApiKeyRepository apiKeyRepository) {
        this.apiKeyRepository = apiKeyRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String apiKeyHeader = request.getHeader("X-API-KEY");
        if(apiKeyHeader == null || apiKeyHeader.isEmpty()){
            setResponse(HttpStatus.UNAUTHORIZED.value(), "INVALID-API-KEY", "X-API-KEY header required", response);
            return;
        }

        ApiKey apiKey = apiKeyRepository.findByApiKey(apiKeyHeader).orElse(null);
        if(apiKey == null){
            setResponse(HttpStatus.UNAUTHORIZED.value(), "INVALID-API-KEY", "Invalid API-KEY", response);
            return;
        }

        if(!apiKey.isActivate()){
            setResponse(HttpStatus.FORBIDDEN.value(), "API-KEY-DISABLED", "API-KEY Activate is false", response);
            return;
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return HttpMethod.POST.matches(request.getMethod()) && request.getRequestURI().equals("/apps");
    }

    private void setResponse(int httpCode, String error, String message, HttpServletResponse response) throws IOException {
        ErrorResponse errorResponse = new ErrorResponse(httpCode, error, message);
        String responseString = mapper.writeValueAsString(errorResponse);
        response.setStatus(httpCode);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.getWriter().write(responseString);
        response.getWriter().flush();
    }

}
