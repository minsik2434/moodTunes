package com.moodtunes.apiserver.repository;

import com.moodtunes.apiserver.entity.ApiKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApiKeyRepository extends JpaRepository<ApiKey, Long> {
}
