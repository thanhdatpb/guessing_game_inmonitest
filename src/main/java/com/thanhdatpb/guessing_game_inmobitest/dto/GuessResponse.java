package com.thanhdatpb.guessing_game_inmobitest.dto;

public record GuessResponse(
        int number,
        int serverNumber,
        boolean correct,
        int score,
        int turns
) {
}