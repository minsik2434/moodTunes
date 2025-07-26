package com.moodtunes.apiserver.service;

import com.moodtunes.apiserver.dto.MusicResponse;
import com.moodtunes.apiserver.entity.Mood;
import com.moodtunes.apiserver.entity.Music;
import com.moodtunes.apiserver.entity.MusicTag;
import com.moodtunes.apiserver.entity.Tag;
import com.moodtunes.apiserver.exception.NotFoundException;
import com.moodtunes.apiserver.repository.MoodRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
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
        Mood mood = new Mood("happy");

        Music music = new Music("좋은날", "IU", mood, "http://example.com");
        Tag ballad = new Tag("발라드");
        Tag rnb = new Tag("R&B/Soul");

        MusicTag musicTag1 = new MusicTag(music, ballad);
        MusicTag musicTag2 = new MusicTag(music, rnb);

        music.addMusicTag(musicTag1);
        music.addMusicTag(musicTag2);

        mood.addMusic(music);

        when(moodRepository.findByMoodName("happy"))
                .thenReturn(Optional.of(mood));

        MusicResponse response = musicService.randomMusicByMood("happy");
        assertThat(response.getTitle()).isEqualTo("좋은날");
        assertThat(response.getArtist()).isEqualTo("IU");
        assertThat(response.getMood()).isEqualTo("happy");
        assertThat(response.getTags()).containsExactly("발라드", "R&B/Soul");
        assertThat(response.getUrl()).isEqualTo("http://example.com");
    }

    @Test
    void randomMusicByMood_notFoundMood(){
        when(moodRepository.findByMoodName(NO_EXIST_MOOD_NAME))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> musicService.randomMusicByMood(NO_EXIST_MOOD_NAME))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("NotFound mood");
    }

    @Test
    void randomMusicByMood_notFoundMusic(){
        Mood emptyMood = new Mood("happy");
        when(moodRepository.findByMoodName("happy"))
                .thenReturn(Optional.of(emptyMood));

        assertThatThrownBy(() -> musicService.randomMusicByMood("happy"))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("No Music found for mood: happy");
    }

    @Test
    void randomMusicTest(){

    }

    @Test
    void randomMusicTest_notFoundMusic(){

        when(moodRepository.findAll())
                .thenReturn(List.of());
        assertThatThrownBy(() -> musicService.randomMusic())
                .isInstanceOf(NotFoundException.class)
                .hasMessage("No Music");
    }
}
