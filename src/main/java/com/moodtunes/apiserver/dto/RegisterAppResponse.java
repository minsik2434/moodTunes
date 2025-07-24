package com.moodtunes.apiserver.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class RegisterAppResponse {
    private Long appId;
    private String apiKey;
    private int quotaLimit;
    private LocalDateTime issuedAt;

    public RegisterAppResponse(Long appId, String apiKey, int quotaLimit, LocalDateTime issuedAt) {
        this.appId = appId;
        this.apiKey = apiKey;
        this.quotaLimit = quotaLimit;
        this.issuedAt = issuedAt;
    }
}
