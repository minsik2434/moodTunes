package com.moodtunes.apiserver.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MusicResponse {
    private String title;
    private String artist;
    private String mood;
    private List<String> tags;
    private String url;

    public MusicResponse(String title, String artist, String mood, List<String> tags, String url) {
        this.title = title;
        this.artist = artist;
        this.mood = mood;
        this.tags = tags;
        this.url = url;
    }
}
