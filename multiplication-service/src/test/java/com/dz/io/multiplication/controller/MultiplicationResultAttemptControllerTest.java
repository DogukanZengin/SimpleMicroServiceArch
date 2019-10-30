package com.dz.io.multiplication.controller;

import com.dz.io.multiplication.domain.Multiplication;
import com.dz.io.multiplication.domain.MultiplicationResultAttempt;
import com.dz.io.multiplication.domain.User;
import com.dz.io.multiplication.service.MultiplicationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@WebMvcTest(MultiplicationResultAttemptController.class)
public class MultiplicationResultAttemptControllerTest {

    @MockBean
    private MultiplicationService multiplicationService;

    @Autowired
    private MockMvc mvc;

    private JacksonTester<MultiplicationResultAttempt> jsonResult;
    private JacksonTester<List<MultiplicationResultAttempt>> jsonResultAttemptList;

    @Before
    public void setUp(){
        JacksonTester.initFields(this, new ObjectMapper());
    }

    @Test
    public void postResultReturnCorrect() throws Exception {
        genericParameterizedTest(true);
    }

    @Test
    public void postResultReturnWrong() throws Exception {
        genericParameterizedTest(false);
    }

    @Test
    public void getUserStats() throws Exception{
        Multiplication multiplication = new Multiplication(50,70);
        User user = new User("Dogukan");
        MultiplicationResultAttempt resultAttempt = new MultiplicationResultAttempt(user, multiplication,3500, true);
        List<MultiplicationResultAttempt> attempts = Lists.newArrayList(resultAttempt,resultAttempt);
        given(multiplicationService.getStatsForUser("Dogukan")).willReturn(attempts);

        MockHttpServletResponse response =  mvc.perform(get("/results").param("alias","Dogukan")).andReturn().getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(jsonResultAttemptList.write(attempts).getJson());
    }

    @Test
    public void getAttemptByIdTest() throws Exception{
        Multiplication multiplication = new Multiplication(50,70);
        User user = new User("Dogukan");
        MultiplicationResultAttempt resultAttempt = new MultiplicationResultAttempt(user, multiplication,3500, true);
        given(multiplicationService.getAttempt(4L)).willReturn(resultAttempt);
        MockHttpServletResponse response =  mvc.perform(get("/results/4")).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(jsonResult.write(resultAttempt).getJson());
    }

    void genericParameterizedTest(final boolean correct) throws Exception {
        given(multiplicationService.checkAttempt(any(MultiplicationResultAttempt.class))).willReturn(correct);

        Multiplication multiplication = new Multiplication(50,70);
        User user = new User("Dogukan");
        MultiplicationResultAttempt resultAttempt = new MultiplicationResultAttempt(user, multiplication,3500, correct);

        MockHttpServletResponse response = mvc.perform(post("/results").contentType(MediaType.APPLICATION_JSON)
                .content(jsonResult.write(resultAttempt).getJson())).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(jsonResult.write(new MultiplicationResultAttempt(resultAttempt.getUser(),
                resultAttempt.getMultiplication(),
                resultAttempt.getResultAttempt(),
                correct)).getJson());
    }
}
