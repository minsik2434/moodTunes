package com.moodtunes.apiserver.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RegisterAppRequest {
    @NotBlank(message = "appName is required")
    private String appName;
    @NotBlank(message = "ownerEmail is required")
    @Email(message = "ownerEmail is not in format")
    private String ownerEmail;
    @NotNull(message = "quotaLimit is required")
    @Min(value = 1, message = "quotaLimit must be greater than or equal to 1")
    private int quotaLimit;

    public RegisterAppRequest(String appName, String ownerEmail, int quotaLimit) {
        this.appName = appName;
        this.ownerEmail = ownerEmail;
        this.quotaLimit = quotaLimit;
    }
}
