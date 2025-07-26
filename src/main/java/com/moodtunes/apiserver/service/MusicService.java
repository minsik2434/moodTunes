package com.moodtunes.apiserver.service;

import com.moodtunes.apiserver.dto.MusicResponse;
import com.moodtunes.apiserver.entity.Mood;
import com.moodtunes.apiserver.entity.Music;
import com.moodtunes.apiserver.exception.NotFoundException;
import com.moodtunes.apiserver.repository.MoodRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MusicService {

    private final MoodRepository moodRepository;

    @Transactional(readOnly = true)
    public MusicResponse randomMusic(String moodName){
        Mood mood = moodRepository.findByMoodName(moodName)
                .orElseThrow(() -> new NotFoundException("NotFound mood"));
        List<Music> musicList = mood.getMusicList();

        return new MusicResponse("좋은날", "IU", "happy", List.of("발라드", "R&B/Soul"),
                "https://youtu.be/jeqdYqsrsA0?si=Z9GjubxbM_U0xkKC");
    }
}
