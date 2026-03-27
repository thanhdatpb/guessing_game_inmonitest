CREATE DATABASE guessing_game;
USE guessing_game;

CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    score INT DEFAULT 0,
    turns INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE guess_history (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    guessed_number INT NOT NULL,
    server_number INT NOT NULL,
    result BOOLEAN NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_user_guess
        FOREIGN KEY (user_id) REFERENCES users(id)
        ON DELETE CASCADE
);

CREATE INDEX idx_score ON users(score DESC);

CREATE INDEX idx_user_id ON guess_history(user_id);

ALTER TABLE guess_history
ADD CONSTRAINT chk_guess_number CHECK (guessed_number BETWEEN 1 AND 5);


INSERT INTO users (username, password, score, turns)
VALUES 
('dat', '$2a$10$9rFiZhnY4OwR1uJRdpQkHetg.X4HDlZRCW.jAK7uAQMSfpCzLP.Ky', 5, 10),
('user1', '$2a$10$9rFiZhnY4OwR1uJRdpQkHetg.X4HDlZRCW.jAK7uAQMSfpCzLP.Ky', 8, 3),
('user2', '$2a$10$9rFiZhnY4OwR1uJRdpQkHetg.X4HDlZRCW.jAK7uAQMSfpCzLP.Ky', 2, 7);