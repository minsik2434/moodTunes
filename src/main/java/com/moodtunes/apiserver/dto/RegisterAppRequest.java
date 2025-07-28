package com.moodtunes.apiserver.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RegisterAppRequest {
    @NotBlank
    private String appName;
    @NotBlank
    private String ownerEmail;
    @NotNull
    @Min(1)
    private int quotaLimit;

    public RegisterAppRequest(String appName, String ownerEmail, int quotaLimit) {
        this.appName = appName;
        this.ownerEmail = ownerEmail;
        this.quotaLimit = quotaLimit;
    }
}
