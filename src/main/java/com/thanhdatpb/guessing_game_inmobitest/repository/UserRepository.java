package com.thanhdatpb.guessing_game_inmobitest.repository;

import com.thanhdatpb.guessing_game_inmobitest.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}