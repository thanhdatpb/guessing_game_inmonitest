package com.thanhdatpb.guessing_game_inmobitest.dto;

public record UserResponse(
        String username,
        String email,
        int score,
        int turns
) {
}