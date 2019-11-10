package com.dz.io.gamification.controller;

import com.dz.io.gamification.domain.ScoreCard;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@RunWith(SpringRunner.class)
@WebMvcTest(ScoreController.class)
public class ScoreControllerTest {

    @MockBean
    private GameService gameService;

    @Autowired
    private MockMvc mvc;

    JacksonTester<ScoreCard> jacksonTester;

    @Before
    public void setUp(){
        JacksonTester.initFields(this, new ObjectMapper());
    }

    @Test
    public void getScoreForAttemptTest() throws Exception{
        ScoreCard scoreCard =  new ScoreCard(1L,100L);
        given(gameService.getScoreForAttempt(100L)).willReturn(scoreCard);

        MockHttpServletResponse response = mvc.perform(get("/scores/100")).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(jacksonTester.write(scoreCard).getJson());
    }
}
