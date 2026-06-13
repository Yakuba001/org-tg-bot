package com.orgtgbot.repository;

import com.orgtgbot.entity.user.StateManager;
import lombok.NonNull;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StateManagerRepository extends JpaRepository<StateManager, Long> {

    @Override
    @Cacheable(cacheNames = "states_cache", key = "#id")
    @NonNull
    Optional<StateManager> findById(@NonNull Long id);

    @Override
    @CachePut(cacheNames = "states_cache", key = "#entity.telegramChatId")
    @NonNull
    <S extends StateManager> S save(@NonNull S entity);
}
