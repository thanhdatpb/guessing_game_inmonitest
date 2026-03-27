package com.thanhdatpb.guessing_game_inmobitest.dto;

public record AuthResponse(
        String token,
        String username
) {
}