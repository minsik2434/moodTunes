package com.moodtunes.apiserver.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RedisServiceTest {

    @InjectMocks
    RedisService redisService;

    @Mock
    RedisTemplate<String, String> redisTemplate;

    @Mock
    ValueOperations<String, String> ops;

    @BeforeEach
    void setUp(){
        when(redisTemplate.opsForValue())
                .thenReturn(ops);
    }

    @Test
    void getValueTest(){
        when(ops.get("key")).thenReturn("value");

        Optional<String> value = redisService.getValue("key");

        assertThat(value.isPresent()).isTrue();
        assertThat(value.get()).isEqualTo("value");
        verify(ops).get("key");
    }

    @Test
    void getValueTest_notFound(){
        when(ops.get("key")).thenReturn(null);
        Optional<String> value = redisService.getValue("key");

        assertThat(value.isEmpty()).isTrue();
        verify(ops).get("key");
    }

    @Test
    void setValueTest(){
        redisService.setValue("key", "value");
        verify(ops).set("key", "value");
    }

    @Test
    void getValuesTest(){
        when(ops.get("key1")).thenReturn("value1");
        when(ops.get("key2")).thenReturn("value2");
        when(ops.get("key3")).thenReturn("value3");

        List<String> values = redisService.getValues("key1", "key2", "key3");

        assertThat(values).containsExactly("value1", "value2", "value3");
        verify(ops, times(3)).get(anyString());
    }

}