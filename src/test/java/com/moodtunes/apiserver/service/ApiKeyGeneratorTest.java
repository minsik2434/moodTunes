package com.moodtunes.apiserver.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class ApiKeyGeneratorTest {

    @InjectMocks
    UuidApiKeyGenerator apiKeyGenerator;

    @Test
    void generateTest(){
        String generate = apiKeyGenerator.generate();
        assertThat(generate).matches( "^[0-9a-fA-F]{16}$");
    }
}
