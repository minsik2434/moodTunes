package com.moodtunes.apiserver.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Music {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String artist;

    @ManyToOne
    @JoinColumn(name = "mood_id")
    private Mood mood;

    @OneToMany(fetch = FetchType.LAZY)
    List<MusicTag> musicTags = new ArrayList<>();

    private String url;

    public Music(String title, String artist, Mood mood, String url){
        this.title = title;
        this.artist = artist;
        this.mood = mood;
        this.url = url;
    }

    public void addMusicTag(MusicTag musicTag){
        musicTags.add(musicTag);
        musicTag.setMusic(this);
    }

    public void setMood(Mood mood){
        this.mood = mood;
    }
}
