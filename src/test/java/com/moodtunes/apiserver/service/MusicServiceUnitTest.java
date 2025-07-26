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

    @Test
    void randomMusicByMood() throws NoSuchFieldException, IllegalAccessException {
    }

    @Test
    void randomMusicByMood_notFound(){
        when(moodRepository.findByMoodName("sad"))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> musicService.randomMusic("sad"))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("NotFound mood");
    }
}
