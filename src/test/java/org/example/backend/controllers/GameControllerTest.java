package org.example.backend.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.backend.dto.ActionDTO;
import org.example.backend.dto.MemberDTO;
import org.example.domain.game.Action;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class GameControllerTest {
    private static final String AUTH_HEADER = "Authorization";
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper mapper;

    private List<String> tokens = new ArrayList<>();
    private String roomId;
    private static int testNumber = 0;

    @BeforeEach
    void createMemberAndLogin() throws Exception {
        for (int i=0; i<3; i++) {
            String body = mapper.writeValueAsString(getTestMemberDTO(i));
            getSignupResult(body);
            MvcResult signinResult = getSigninResult(body);
            tokens.add(getBearerToken(signinResult));
            makePlayer(i);
            if (i == 2)
                break;
            enterAutoRoom(i);
            ready(i);
        }
        roomId = getRoomId(enterAutoRoom(2));
        ready(2);
    }

    private MemberDTO getTestMemberDTO(int testNumber) {
        return MemberDTO.builder()
                .email("test" + testNumber)
                .nickname("test" + testNumber)
                .password("1234")
                .build();
    }

    private MvcResult getSignupResult(String body) throws Exception {
        return mvc.perform(post("/auth/signup")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    }

    private MvcResult getSigninResult(String body) throws Exception {
        return mvc.perform(post("/auth/signin")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    }

    private String getBearerToken(MvcResult result) throws Exception {
        return "Bearer " + result.getResponse().getContentAsString()
                .replace("{\"token\":\"", "")
                .split("\",")[0];
    }

    private void makePlayer(int tokenIndex) throws Exception {
        mvc.perform(post("/room/player-maker")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", tokens.get(tokenIndex)))
                .andExpect(status().isOk());
    }

    private MvcResult enterAutoRoom(int tokenIndex) throws Exception {
        return mvc.perform(post("/room/auto-enter")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", tokens.get(tokenIndex)))
                .andExpect(status().isOk())
                .andReturn();
    }

    private void ready(int tokenIndex) throws Exception {
        mvc.perform(post("/room/player-status-change")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", tokens.get(tokenIndex)))
                .andExpect(status().isOk());
    }

    private String getRoomId(MvcResult result) throws Exception {
        return result.getResponse().getContentAsString()
                .replace("{\"roomId\":\"", "")
                .split("\",")[0];
    }

    @Test
    @DisplayName("Make game Test")
    void testMakeGame() throws Exception {
        makeGame();
    }

    @Test
    @DisplayName("Get game Test")
    void testGetGame() throws Exception {
        makeGame();
        MvcResult result = mvc.perform(get("/game/result")
                        .header(AUTH_HEADER, tokens.get(0))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    @DisplayName("Play action Test")
    void testPlayAction() throws Exception {
        makeGame();
        ActionDTO action = ActionDTO.builder()
                .action(Action.CALL)
                .betSize(0)
                .build();
        String body = mapper.writeValueAsString(action);
        MvcResult result = mvc.perform(get("/game/result")
                        .header(AUTH_HEADER, tokens.get(0))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    @DisplayName("Leave game Test")
    void testLeaveGame() throws Exception {
        makeGame();
        mvc.perform(post("/game/leave")
                        .header(AUTH_HEADER, tokens.get(0))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Get hands Test")
    void testGetHands() throws Exception {
        makeGame();
        mvc.perform(get("/game/hands")
                        .header(AUTH_HEADER, tokens.get(0))
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andReturn();
    }

    private void makeGame() throws Exception {
        mvc.perform(post("/game/game")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(AUTH_HEADER, tokens.get(0)))
                .andExpect(status().isOk());
    }
}