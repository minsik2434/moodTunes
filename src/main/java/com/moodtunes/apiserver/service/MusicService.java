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
    private static final String NOT_FOUND_MOOD_MESSAGE = "NotFound mood";
    private static final String NO_MUSIC_FOR_MOOD_MESSAGE_PREFIX = "No Music found for mood: ";
    private static final String NO_MUSIC_MESSAGE = "No Music";

    @Transactional(readOnly = true)
    public MusicResponse randomMusicByMood(String moodName){
        Mood mood =
                moodRepository.findByMoodName(moodName).orElseThrow(() -> new NotFoundException(NOT_FOUND_MOOD_MESSAGE));

        List<Music> musicList = mood.getMusicList();
        return getRandomMusicResponse(musicList, NO_MUSIC_FOR_MOOD_MESSAGE_PREFIX + moodName);
    }

    @Transactional(readOnly = true)
    public MusicResponse randomMusic(){
        List<Music> musicList = musicRepository.findAll();
        return getRandomMusicResponse(musicList, NO_MUSIC_MESSAGE);
    }

    private MusicResponse getRandomMusicResponse(List<Music> musicList, String errorMessage){
        if(musicList.isEmpty()){
            throw new NotFoundException(errorMessage);
        }

        Music selected = findRandomMusic(musicList);
        return createMusicResponse(selected);
    }

    private Music findRandomMusic(List<Music> musicList){
        int randomIndex = ThreadLocalRandom.current().nextInt(musicList.size());
        return musicList.get(randomIndex);
    }

    private List<String> getMusicTagList(Music selected) {
        return selected.getMusicTags().stream()
                .map(mt -> mt.getTag().getTagName())
                .toList();
    }

    private MusicResponse createMusicResponse(Music selected){
        return new MusicResponse(
                selected.getTitle(),
                selected.getArtist(),
                selected.getMood().getMoodName(),
                getMusicTagList(selected),
                selected.getUrl());
    }
}
