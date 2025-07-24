package com.moodtunes.apiserver.service;

import org.springframework.stereotype.Component;

import java.util.UUID;

public class KeyGenerator {
    public static String generate(){
        return UUID.randomUUID().toString();
    }
}
