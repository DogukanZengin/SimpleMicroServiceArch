package com.dz.io.multiplication.controller;

import com.dz.io.multiplication.domain.User;
import com.dz.io.multiplication.repository.UserRepository;
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

import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private MockMvc mvc;

    private JacksonTester<User> jacksonTester;

    @Before
    public void setUp(){
        JacksonTester.initFields(this,new ObjectMapper());
    }

    @Test
    public void getUserTest() throws Exception{
        User user = new User("Dogukan");
        given(userRepository.findById(100L)).willReturn(Optional.of(user));

        MockHttpServletResponse result = mvc.perform(get("/users/100")).andReturn().getResponse();

        assertThat(result.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(result.getContentAsString()).isEqualTo(jacksonTester.write(user).getJson());
    }
}
