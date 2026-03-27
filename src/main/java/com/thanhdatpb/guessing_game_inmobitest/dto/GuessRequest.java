package com.thanhdatpb.guessing_game_inmobitest.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record GuessRequest(
        @NotNull(message = "number is required")
        @Min(value = 1, message = "number must be between 1 and 5")
        @Max(value = 5, message = "number must be between 1 and 5")
        Integer number
) {
}