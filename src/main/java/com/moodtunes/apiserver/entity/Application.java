package com.moodtunes.apiserver.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String ownerEmail;
    private LocalDateTime createAt;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "application", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ApiKey> apiKeys = new ArrayList<>();

    public Application(String name, String ownerEmail) {
        this.name = name;
        this.ownerEmail = ownerEmail;
    }

    public void addApiKey(String apiKeyString, int quotaLimit, boolean activate){
        ApiKey apiKey = new ApiKey(this, apiKeyString, quotaLimit, activate);
        apiKeys.add(apiKey);
    }
}
