package com.thanhdatpb.guessing_game_inmobitest.dto;

public record UserResponse(
        String username,
        int score,
        int turns
) {
}