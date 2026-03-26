package com.thanhdatpb.guessing_game_inmobitest.repository;

import com.thanhdatpb.guessing_game_inmobitest.entity.GuessHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GuessHistoryRepository extends JpaRepository<GuessHistory, Long> {
}
