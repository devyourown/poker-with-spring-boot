package org.example.backend.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.poker.backend.dto.MemberDTO;
import org.poker.backend.dto.RoomDTO;
import org.poker.backend.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class RoomControllerTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MemberService memberService;
    private String token;
    private String roomId;
    private static int testNumber = 1;

    @BeforeEach
    void createMemberAndLogin() throws Exception {
        String body = mapper.writeValueAsString(getTestMemberDTO(testNumber++));
        getSignupResult(body);
        MvcResult signinResult = getSigninResult(body);
        token = getBearerToken(signinResult);
        makePlayer();
        MvcResult roomResult = enterAutoRoom();
        roomId = getRoomId(roomResult);
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

    private void makePlayer() throws Exception {
        mvc.perform(post("/room/player-maker")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", token))
                .andExpect(status().isOk());
    }

    private MvcResult enterAutoRoom() throws Exception {
        return mvc.perform(post("/room/auto-enter")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", token))
                .andExpect(status().isOk())
                .andReturn();
    }

    private String getRoomId(MvcResult result) throws Exception {
        return result.getResponse().getContentAsString()
                .replace("{\"roomId\":\"", "")
                .split("\",")[0];
    }

    @Test
    @DisplayName("Get current room status Test")
    void testGetStatus() throws Exception {
        String body = mapper.writeValueAsString(getRoomDTOWithId());
        mvc.perform(post("/room/room-status")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Change Player Status Test")
    void testReady() throws Exception {
        String body = mapper.writeValueAsString(getRoomDTOWithId());
        mvc.perform(post("/room/player-status-change")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk());
    }

    private RoomDTO getRoomDTOWithId() {
        return RoomDTO.builder()
                .roomId(roomId)
                .build();
    }
}