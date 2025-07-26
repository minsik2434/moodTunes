package com.moodtunes.apiserver.repository;

import com.moodtunes.apiserver.entity.Music;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MusicRepository extends JpaRepository<Music, Long> {

}
