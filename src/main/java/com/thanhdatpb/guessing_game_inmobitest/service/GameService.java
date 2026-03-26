package com.thanhdatpb.guessing_game_inmobitest.service;

import com.thanhdatpb.guessing_game_inmobitest.entity.User;
import com.thanhdatpb.guessing_game_inmobitest.repository.GuessHistoryRepository;
import com.thanhdatpb.guessing_game_inmobitest.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class GameService {

    private final UserRepository userRepository;
    private final GuessHistoryRepository historyRepository;


    @Transactional
    public Map<String, Object> guess(String username) {

        // lock user row
        User user = userRepository.findByUsernameForUpdate(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // validate input
        if (number < 1 || number > 5) {
            throw new RuntimeException("Number must be between 1 and 5");
        }

        // check turns
        if (user.getTurns() <= 0) {
            throw new RuntimeException("No turns left");
        }

        // trừ turn
        user.setTurns(user.getTurns() - 1);

        // logic 5% win
        boolean win = Math.random() < 0.05;
        boolean correct;

        if (win) {
            correct = true;
            user.setScore(user.getScore() + 1);
        } else {
            correct = false;
        }

        userRepository.save(user);

        return Map.of(
                "correct", correct,
                "score", user.getScore(),
                "remainingTurns", user.getTurns()
        );
    }
}