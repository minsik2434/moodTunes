package com.moodtunes.apiserver.service;

import com.moodtunes.apiserver.dto.MusicResponse;
import com.moodtunes.apiserver.entity.Mood;
import com.moodtunes.apiserver.entity.Music;
import com.moodtunes.apiserver.exception.NotFoundException;
import com.moodtunes.apiserver.repository.MoodRepository;
import com.moodtunes.apiserver.repository.MusicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class MusicService {

    private final MoodRepository moodRepository;
    private final MusicRepository musicRepository;

    @Transactional(readOnly = true)
    public MusicResponse randomMusicByMood(String moodName){
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

    @Transactional
    public MusicResponse randomMusic(){
        List<Music> musicList = musicRepository.findAll();
        if (musicList.isEmpty()){
            throw new NotFoundException("No Music");
        }

        int randomIndex = ThreadLocalRandom.current().nextInt(musicList.size());
        Music selected = musicList.get(randomIndex);

        List<String> tagList = selected.getMusicTags().stream()
                .map(mt -> mt.getTag().getTagName())
                .toList();

        return new MusicResponse(selected.getTitle(), selected.getArtist(), selected.getMood().getMoodName(), tagList,
                selected.getUrl());
    }
}
