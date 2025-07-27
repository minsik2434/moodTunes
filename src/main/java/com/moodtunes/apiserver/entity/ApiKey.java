package com.moodtunes.apiserver.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EntityListeners(AuditingEntityListener.class)
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
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime issuedAt;

    public ApiKey(String apiKey, int quotaLimit, boolean activate){
        this.apiKey = apiKey;
        this.quotaLimit = quotaLimit;
        this.activate = activate;
    }

    public void setApplication(Application application){
        this.application = application;
    }
}
