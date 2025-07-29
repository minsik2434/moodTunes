package com.moodtunes.apiserver.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

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

    private long secondsUntilNextMidnight(){
        ZonedDateTime now = ZonedDateTime.now(ZoneId.systemDefault());
        ZonedDateTime midNight = now.plusDays(1).truncatedTo(ChronoUnit.DAYS);
        return Duration.between(now, midNight).getSeconds();
    }

    public long incrementWithMidnightExpire(String key){
        long newCount = redisTemplate.opsForValue().increment(key);

        Long ttl = redisTemplate.getExpire(key, TimeUnit.SECONDS);
        if (ttl < 0) {
            redisTemplate.expire(key, secondsUntilNextMidnight(), TimeUnit.SECONDS);
        }
        return newCount;
    }

    @Transactional
    public void setValue(String key, String value, Duration duration){
        ValueOperations<String, String> vo = redisTemplate.opsForValue();
        long seconds = duration.getSeconds();
        vo.set(key, value, seconds, TimeUnit.SECONDS);
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

    public Long increment(String key){
        return redisTemplate.opsForValue().increment(key);
    }

    @Transactional
    public void deleteValue(String key){
        redisTemplate.delete(key);
    }

    @Transactional
    public void deleteValues(List<String> keys){
        redisTemplate.delete(keys);
    }
}
