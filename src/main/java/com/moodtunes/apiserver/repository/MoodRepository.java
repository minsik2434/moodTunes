package com.moodtunes.apiserver.repository;

import com.moodtunes.apiserver.entity.Mood;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MoodRepository extends JpaRepository<Mood, Long> {
    Optional<Mood> findByMoodName(String moodName);
}
