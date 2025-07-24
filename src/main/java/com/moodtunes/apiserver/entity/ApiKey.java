package com.moodtunes.apiserver.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ApiKey {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "app_id")
    private Application application;
    private String apiKey;
    private int quotaLimit;
    private boolean activate;
    private LocalDateTime issuedAt;

    public ApiKey(Application application, String apiKey, int quotaLimit, boolean activate) {
        this.application = application;
        this.apiKey = apiKey;
        this.quotaLimit = quotaLimit;
        this.activate = activate;
    }
}
