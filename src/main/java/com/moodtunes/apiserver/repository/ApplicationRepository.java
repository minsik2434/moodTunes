package com.moodtunes.apiserver.repository;

import com.moodtunes.apiserver.entity.Application;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
}
