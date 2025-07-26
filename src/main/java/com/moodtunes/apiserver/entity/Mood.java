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
public class Mood {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String moodName;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "mood")
    List<Music> musicList = new ArrayList<>();

    public Mood(String moodName) {
        this.moodName = moodName;
    }

    public void addMusic(Music music){
        musicList.add(music);
        music.setMood(this);
    }
}
