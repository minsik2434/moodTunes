package com.moodtunes.apiserver.controller;

import com.moodtunes.apiserver.dto.ApplicationInfoResponse;
import com.moodtunes.apiserver.dto.RegisterAppRequest;
import com.moodtunes.apiserver.dto.RegisterAppResponse;
import com.moodtunes.apiserver.service.ApplicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/apps")
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService applicationService;

    @PostMapping
    public ResponseEntity<RegisterAppResponse> registerApplication(@Valid @RequestBody RegisterAppRequest request){
        RegisterAppResponse response = applicationService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{appId}")
    public ResponseEntity<ApplicationInfoResponse> infoApplication(@PathVariable("appId") Long id){
        ApplicationInfoResponse info = applicationService.getInfo(id);
        return ResponseEntity.ok(info);
    }
}
