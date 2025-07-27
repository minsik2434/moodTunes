package com.moodtunes.apiserver.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Setter
@Getter
public class ApplicationInfoResponse {
    private Long appId;
    private String name;
    private String ownerEmail;
    private List<ApiKeyDto> apiKeys;

    public ApplicationInfoResponse(Long appId, String name, String ownerEmail, List<ApiKeyDto> apiKeys) {
        this.appId = appId;
        this.name = name;
        this.ownerEmail = ownerEmail;
        this.apiKeys = apiKeys;
    }
}
