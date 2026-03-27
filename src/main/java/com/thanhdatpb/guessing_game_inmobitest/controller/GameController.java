package com.thanhdatpb.guessing_game_inmobitest.controller;

import com.thanhdatpb.guessing_game_inmobitest.dto.GuessRequest;
import com.thanhdatpb.guessing_game_inmobitest.dto.GuessResponse;
import com.thanhdatpb.guessing_game_inmobitest.dto.UserResponse;
import com.thanhdatpb.guessing_game_inmobitest.service.GameService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Game", description = "Protected game endpoints that require a JWT bearer token")
public class GameController {

    private final GameService gameService;

    @PostMapping("/guess")
    @Operation(
            summary = "Submit a guess",
            description = "Consumes one turn, compares the user input with the server number, and updates score if the guess is correct.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<GuessResponse> guess(
            @Valid @RequestBody GuessRequest request,
            Authentication authentication
    ) {
        return ResponseEntity.ok(gameService.guess(authentication.getName(), request));
    }

    @PostMapping("/buy-turns")
    @Operation(
            summary = "Buy more turns",
            description = "Adds more turns for the authenticated user.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<UserResponse> buyTurns(Authentication authentication) {
        return ResponseEntity.ok(gameService.buyTurns(authentication.getName()));
    }

    @GetMapping("/leaderboard")
    @Operation(
            summary = "Get leaderboard",
            description = "Returns the top 10 users ordered by score.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<List<UserResponse>> leaderboard() {
        return ResponseEntity.ok(gameService.getLeaderboard());
    }

    @GetMapping("/me")
    @Operation(
            summary = "Get current user profile",
            description = "Returns the current authenticated user score and remaining turns.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<UserResponse> me(Authentication authentication) {
        return ResponseEntity.ok(gameService.getCurrentUser(authentication.getName()));
    }
}