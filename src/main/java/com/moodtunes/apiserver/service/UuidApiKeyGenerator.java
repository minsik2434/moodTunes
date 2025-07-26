package com.moodtunes.apiserver.service;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UuidApiKeyGenerator implements ApiKeyGenerator{

    @Override
    public String generate() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 16);
    }
}
