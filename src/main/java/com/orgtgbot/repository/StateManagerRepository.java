package com.orgtgbot.repository;

import com.orgtgbot.entity.user.StateManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StateManagerRepository extends JpaRepository<StateManager, Long> {

}
