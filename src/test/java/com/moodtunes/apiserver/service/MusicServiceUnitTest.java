package com.moodtunes.apiserver.service;

import com.moodtunes.apiserver.dto.MusicResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class MusicServiceUnitTest {

    @InjectMocks
    MusicService musicService;

    @Test
    void randomMusicByMood(){
        String requestMood = "happy";
        MusicResponse response = musicService.randomMusic(requestMood);

        assertThat(response.getTitle()).isEqualTo("좋은날");
        assertThat(response.getArtist()).isEqualTo("IU");
        assertThat(response.getMood()).isEqualTo("happy");
        assertThat(response.getTags()).containsExactlyInAnyOrder("발라드", "R&B/Soul");
        assertThat(response.getUrl()).isEqualTo("https://youtu.be/jeqdYqsrsA0?si=Z9GjubxbM_U0xkKC");

    }
}
