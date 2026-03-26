package com.thanhdatpb.guessing_game_inmobitest.controller;

import com.thanhdatpb.guessing_game_inmobitest.entity.User;
import com.thanhdatpb.guessing_game_inmobitest.repository.UserRepository;
import com.thanhdatpb.guessing_game_inmobitest.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody Map<String, String> request) {

        String username = request.get("username");
        String password = request.get("password");

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.getPassword().equals(password)) {
            throw new RuntimeException("Wrong password");
        }

        String token = jwtUtil.generateToken(username);

        return Map.of("token", token);
    }
}