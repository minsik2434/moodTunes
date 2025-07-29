package com.moodtunes.apiserver.controller;

import com.moodtunes.apiserver.dto.MusicResponse;
import com.moodtunes.apiserver.service.MusicService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/music")
public class MusicController {
    private final MusicService musicService;

    @GetMapping("/random")
    public ResponseEntity<MusicResponse> getRandomMusic(){
        MusicResponse response = musicService.randomMusic();
        return ResponseEntity.ok(response);
    }
}
