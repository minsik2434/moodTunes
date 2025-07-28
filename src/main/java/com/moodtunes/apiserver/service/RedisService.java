package com.moodtunes.apiserver.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RedisService {
    private final RedisTemplate<String, String> redisTemplate;

    @Transactional(readOnly = true)
    public Optional<String> getValue(String key){
        ValueOperations<String, String> vo = redisTemplate.opsForValue();
        String result = vo.get(key);
        return Optional.ofNullable(result);
    }

    @Transactional
    public void setValue(String key, String value){
        ValueOperations<String, String> vo = redisTemplate.opsForValue();
        vo.set(key, value);
    }

    @Transactional(readOnly = true)
    public List<String> getValues(String... keys){
        ValueOperations<String, String> vo = redisTemplate.opsForValue();
        List<String> result = new ArrayList<>();
        for (String key : keys) {
            String value = vo.get(key);
            if(value != null){
                result.add(value);
            }
        }

        return result;
    }
}
