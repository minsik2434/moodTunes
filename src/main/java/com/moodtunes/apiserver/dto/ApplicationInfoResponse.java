package com.moodtunes.apiserver.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class ApplicationInfoResponse {
    private Long appId;
    private String name;
    private String ownerEmail;
    private int quotaLimit;
    private int remainingQuota;
    private boolean active;
    private LocalDateTime issuedAt;

    public ApplicationInfoResponse(Long appId, String name, String ownerEmail, int quotaLimit, int remainingQuota,
                                   boolean active, LocalDateTime issuedAt) {
        this.appId = appId;
        this.name = name;
        this.ownerEmail = ownerEmail;
        this.quotaLimit = quotaLimit;
        this.remainingQuota = remainingQuota;
        this.active = active;
        this.issuedAt = issuedAt;
    }
}
