package com.thanhdatpb.guessing_game_inmobitest.service;

import com.thanhdatpb.guessing_game_inmobitest.dto.GuessRequest;
import com.thanhdatpb.guessing_game_inmobitest.dto.GuessResponse;
import com.thanhdatpb.guessing_game_inmobitest.dto.UserResponse;
import com.thanhdatpb.guessing_game_inmobitest.entity.GuessHistory;
import com.thanhdatpb.guessing_game_inmobitest.entity.User;
import com.thanhdatpb.guessing_game_inmobitest.repository.GuessHistoryRepository;
import com.thanhdatpb.guessing_game_inmobitest.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class GameService {

    private static final int MIN_NUMBER = 1;
    private static final int MAX_NUMBER = 5;

    private final UserRepository userRepository;
    private final GuessHistoryRepository historyRepository;
    private final GameRandomizer gameRandomizer;

    @Value("${game.win-probability:0.05}")
    private double winProbability;

    @Value("${game.purchase-turns:5}")
    private int purchaseTurns;

    @Transactional
    public GuessResponse guess(Long userId, GuessRequest request) {
        User user = userRepository.findByIdForUpdate(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        if (user.getTurns() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No turns left");
        }

        int guessedNumber = request.number();
        int serverNumber = generateServerNumber(guessedNumber);
        boolean correct = guessedNumber == serverNumber;

        user.setTurns(user.getTurns() - 1);
        if (correct) {
            user.setScore(user.getScore() + 1);
        }

        GuessHistory history = new GuessHistory();
        history.setUser(user);
        history.setGuessedNumber(guessedNumber);
        history.setServerNumber(serverNumber);
        history.setResult(correct);

        historyRepository.save(history);

        return new GuessResponse(
                guessedNumber,
                serverNumber,
                correct,
                user.getScore(),
                user.getTurns()
        );
    }

    @Transactional
    public UserResponse buyTurns(Long userId) {
        User user = userRepository.findByIdForUpdate(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        user.setTurns(user.getTurns() + purchaseTurns);
        return toUserResponse(user);
    }

    @Transactional(readOnly = true)
    public List<UserResponse> getLeaderboard() {
        List<User> topUsers = userRepository.findAll(
                PageRequest.of(0, 10, Sort.by(Sort.Order.desc("score"), Sort.Order.asc("id")))
        ).getContent();

        return topUsers.stream()
                .map(this::toUserResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public UserResponse getCurrentUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        return toUserResponse(user);
    }

    private UserResponse toUserResponse(User user) {
        return new UserResponse(user.getUsername(), user.getEmail(), user.getScore(), user.getTurns());
    }

    private int generateServerNumber(int guessedNumber) {
        if (gameRandomizer.nextDouble() < winProbability) {
            return guessedNumber;
        }

        int[] losingNumbers = IntStream.rangeClosed(MIN_NUMBER, MAX_NUMBER)
                .filter(number -> number != guessedNumber)
                .toArray();

        return losingNumbers[gameRandomizer.nextInt(losingNumbers.length)];
    }
}