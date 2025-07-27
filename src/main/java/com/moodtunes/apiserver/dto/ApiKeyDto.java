package com.moodtunes.apiserver.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ApiKeyDto {
    private Long id;
    private String keyPrefix;
    private int quotaLimit;
    private int remainingQuota;
    private boolean activate;
    private LocalDateTime issuedAt;

    public ApiKeyDto(Long id, String keyPrefix, int quotaLimit, int remainingQuota, boolean activate, LocalDateTime issuedAt) {
        this.id = id;
        this.keyPrefix = keyPrefix;
        this.quotaLimit = quotaLimit;
        this.remainingQuota = remainingQuota;
        this.activate = activate;
        this.issuedAt = issuedAt;
    }
}
