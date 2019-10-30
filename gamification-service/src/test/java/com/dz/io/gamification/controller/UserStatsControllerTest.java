package com.dz.io.gamification.controller;


import com.dz.io.gamification.domain.Badge;
import com.dz.io.gamification.domain.GameStats;
import com.dz.io.gamification.service.GameService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@RunWith(SpringRunner.class)
@WebMvcTest(UserStatsController.class)
public class UserStatsControllerTest {

    @MockBean
    GameService gameService;

    @Autowired
    private MockMvc mvc;

    private JacksonTester<GameStats> jsonResult;

    @Before
    public void setUp(){
        JacksonTester.initFields(this, new ObjectMapper());
    }

    @Test
    public void getStatsForUserTest() throws Exception{

        GameStats gameStats =  new GameStats(1L, 45, Collections.singletonList(Badge.FIRST_WON));

        given(gameService.retrieveStatsForUser(1L)).willReturn(gameStats);

        MockHttpServletResponse response = mvc.perform(get("/stats").param("userId","1")).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(jsonResult.write(gameStats).getJson());
    }

}
