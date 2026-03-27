package com.thanhdatpb.guessing_game_inmobitest.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.Instant;

@Entity
@Table(name = "guess_history")
@Getter
@Setter
public class GuessHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "guessed_number", nullable = false)
    private Integer guessedNumber;

    @Column(name = "server_number", nullable = false)
    private Integer serverNumber;

    @Column(nullable = false)
    private Boolean result;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @PrePersist
    public void prePersist() {
        createdAt = Instant.now();
    }
}