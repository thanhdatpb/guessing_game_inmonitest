package com.thanhdatpb.guessing_game_inmobitest.service;

import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadLocalRandom;

@Component
public class GameRandomizer {

    public double nextDouble() {
        return ThreadLocalRandom.current().nextDouble();
    }

    public int nextInt(int bound) {
        return ThreadLocalRandom.current().nextInt(bound);
    }
}