package com.thanhdatpb.guessing_game_inmobitest;

import com.thanhdatpb.guessing_game_inmobitest.entity.User;
import com.thanhdatpb.guessing_game_inmobitest.repository.GuessHistoryRepository;
import com.thanhdatpb.guessing_game_inmobitest.repository.UserRepository;
import com.thanhdatpb.guessing_game_inmobitest.security.JwtUtil;
import com.thanhdatpb.guessing_game_inmobitest.service.GameRandomizer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class GuessingGameApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GuessHistoryRepository guessHistoryRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @MockitoBean
    private GameRandomizer gameRandomizer;

    @BeforeEach
    void setUp() {
        guessHistoryRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void registerShouldCreateUserAndReturnToken() throws Exception {
        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"username":"alice","password":"secret123"}
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.token").isString())
                .andExpect(jsonPath("$.username").value("alice"));
    }

    @Test
    void openApiDocsShouldBePublic() throws Exception {
        mockMvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.info.title").value("Guessing Game API"))
                .andExpect(jsonPath("$.paths['/login']").exists())
                .andExpect(jsonPath("$.paths['/guess']").exists());
    }

    @Test
    void loginShouldReturnTokenForValidCredentials() throws Exception {
        saveUser("alice", "secret123", 1, 2);

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"username":"alice","password":"secret123"}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isString())
                .andExpect(jsonPath("$.username").value("alice"));
    }

    @Test
    void buyTurnsAndGuessShouldUpdateUserState() throws Exception {
        User user = saveUser("alice", "secret123", 0, 0);
        String token = bearerToken(user.getUsername());

        mockMvc.perform(post("/buy-turns")
                        .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("alice"))
                .andExpect(jsonPath("$.score").value(0))
                .andExpect(jsonPath("$.turns").value(5));

        when(gameRandomizer.nextDouble()).thenReturn(0.01);

        mockMvc.perform(post("/guess")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"number":3}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.number").value(3))
                .andExpect(jsonPath("$.serverNumber").value(3))
                .andExpect(jsonPath("$.correct").value(true))
                .andExpect(jsonPath("$.score").value(1))
                .andExpect(jsonPath("$.turns").value(4));
    }

    @Test
    void leaderboardAndMeShouldReturnExpectedData() throws Exception {
        saveUser("alice", "secret123", 3, 1);
        saveUser("bob", "secret123", 8, 2);
        saveUser("charlie", "secret123", 5, 9);

        String token = bearerToken("bob");

        mockMvc.perform(get("/leaderboard")
                        .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].email").value("bob"))
                .andExpect(jsonPath("$[0].score").value(8))
                .andExpect(jsonPath("$[1].email").value("charlie"))
                .andExpect(jsonPath("$[2].email").value("alice"));

        mockMvc.perform(get("/me")
                        .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("bob"))
                .andExpect(jsonPath("$.score").value(8))
                .andExpect(jsonPath("$.turns").value(2));
    }

    private User saveUser(String username, String rawPassword, int score, int turns) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setScore(score);
        user.setTurns(turns);
        return userRepository.save(user);
    }

    private String bearerToken(String username) {
        return "Bearer " + jwtUtil.generateToken(username);
    }
}
