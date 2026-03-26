package com.thanhdatpb.guessing_game_inmobitest.controller;

import com.thanhdatpb.guessing_game_inmobitest.entity.User;
import com.thanhdatpb.guessing_game_inmobitest.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class GameController {

    private final GameService gameService;

    @PostMapping("/buy-turns")
    public ResponseEntity<?> buyTurns(@RequestParam Long userId) {
        User user = userRepository.findById(userId).orElseThrow();

        user.setTurns(user.getTurns() + 5);
        userRepository.save(user);

        return ResponseEntity.ok("Bought 5 turns");
    }

    @PostMapping("/guess")
    public ResponseEntity<?> guess(
            @RequestParam Long userId,
            @RequestParam int number
    )

    {
        return ResponseEntity.ok(gameService.guess(userId, number));
    }
}
