package com.moodtunes.apiserver.controller;

import com.moodtunes.apiserver.dto.RegisterAppRequest;
import com.moodtunes.apiserver.dto.RegisterAppResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/apps")
public class ApplicationController {

    @PostMapping
    public ResponseEntity<RegisterAppResponse> registerApplication(@RequestBody RegisterAppRequest request){
        return null;
    }
}
