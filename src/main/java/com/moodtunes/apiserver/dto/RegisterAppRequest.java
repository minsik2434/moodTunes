package com.moodtunes.apiserver.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class RegisterAppRequest {
    private String appName;
    private String ownerEmail;
    private int quotaLimit;

    public RegisterAppRequest(String appName, String ownerEmail, int quotaLimit) {
        this.appName = appName;
        this.ownerEmail = ownerEmail;
        this.quotaLimit = quotaLimit;
    }
}
