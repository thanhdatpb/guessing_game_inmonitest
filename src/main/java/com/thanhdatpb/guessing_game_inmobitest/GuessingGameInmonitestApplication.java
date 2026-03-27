package com.thanhdatpb.guessing_game_inmobitest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.security.autoconfigure.UserDetailsServiceAutoConfiguration;

@SpringBootApplication(exclude = UserDetailsServiceAutoConfiguration.class)
public class GuessingGameInmonitestApplication {

    public static void main(String[] args) {
        SpringApplication.run(GuessingGameInmonitestApplication.class, args);
    }

}