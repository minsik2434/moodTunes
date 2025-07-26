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
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MusicService {

    private final MoodRepository moodRepository;

    @Transactional(readOnly = true)
    public MusicResponse randomMusic(String moodName){
        Mood mood =
                moodRepository.findByMoodName(moodName).orElseThrow(() -> new NotFoundException("NotFound mood"));
        List<Music> musicList = mood.getMusicList();
        if(musicList.isEmpty()){
            throw new NotFoundException("No Music found for mood: " + moodName);
        }

        int randomIndex = ThreadLocalRandom.current().nextInt(musicList.size());
        Music selected = musicList.get(randomIndex);

        List<String> tagList = selected.getMusicTags().stream()
                .map(mt -> mt.getTag().getTagName())
                .toList();

        return new MusicResponse(selected.getTitle(), selected.getArtist(), mood.getMoodName(), tagList,
                selected.getUrl());
    }
}
