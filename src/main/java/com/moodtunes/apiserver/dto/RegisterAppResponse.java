package com.moodtunes.apiserver.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class RegisterAppResponse {
    private Long appId;
    private String apiKey;
    private int quotaLimit;
    private LocalDateTime issuedAt;
}
