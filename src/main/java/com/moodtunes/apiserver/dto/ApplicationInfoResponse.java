package com.moodtunes.apiserver.dto;

import lombok.Getter;
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
}
