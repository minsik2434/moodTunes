package com.moodtunes.apiserver.service;

import com.moodtunes.apiserver.dto.MusicResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MusicService {

    @Transactional(readOnly = true)
    public MusicResponse randomMusic(String mood){
        return new MusicResponse("좋은날", "IU", "happy", List.of("발라드", "R&B/Soul"),
                "https://youtu.be/jeqdYqsrsA0?si=Z9GjubxbM_U0xkKC");
    }
}
