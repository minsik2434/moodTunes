package com.moodtunes.apiserver.service;

import com.moodtunes.apiserver.dto.MusicResponse;
import com.moodtunes.apiserver.entity.Mood;
import com.moodtunes.apiserver.entity.Music;
import com.moodtunes.apiserver.exception.NotFoundException;
import com.moodtunes.apiserver.repository.MoodRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MusicServiceUnitTest {

    @InjectMocks
    MusicService musicService;

    @Mock
    MoodRepository moodRepository;

    private static final String NO_EXIST_MOOD_NAME = "noExistMoodName";

    @Test
    void randomMusicByMood() {
    }

    @Test
    void randomMusicByMood_notFoundMood(){
        when(moodRepository.findByMoodName(NO_EXIST_MOOD_NAME))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> musicService.randomMusic(NO_EXIST_MOOD_NAME))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("NotFound mood");
    }

    @Test
    void randomMusicByMood_notFoundMusic(){
        Mood emptyMood = new Mood("happy");
        when(moodRepository.findByMoodName("happy"))
                .thenReturn(Optional.of(emptyMood));

        assertThatThrownBy(() -> musicService.randomMusic("happy"))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("No Music found for mood: happy");
    }
}
