package com.dz.io.multiplication.service;

import com.dz.io.multiplication.domain.Multiplication;
import com.dz.io.multiplication.domain.MultiplicationResultAttempt;
import com.dz.io.multiplication.domain.User;
import com.dz.io.multiplication.event.EventDispatcher;
import com.dz.io.multiplication.event.MultiplicationSolvedEvent;
import com.dz.io.multiplication.repository.MultiplicationRepository;
import com.dz.io.multiplication.repository.MultiplicationResultAttemptRepository;
import com.dz.io.multiplication.repository.UserRepository;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.given;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

public class MultiplicationServiceImplTest {

    @Mock
    private RandomGeneratorService randomGeneratorService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private MultiplicationResultAttemptRepository multiplicationResultAttemptRepository;

    @Mock
    private MultiplicationRepository multiplicationRepository;

    @Mock
    private EventDispatcher eventDispatcher;

    private MultiplicationServiceImpl multiplicationServiceImpl;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        multiplicationServiceImpl = new MultiplicationServiceImpl(randomGeneratorService,
                multiplicationResultAttemptRepository,
                userRepository,
                multiplicationRepository, eventDispatcher);
    }

    @Test
    public void checkCorrectAttemptTest(){
        //GIVEN
        Multiplication multiplication = new Multiplication(50,60);
        User user = new User("Dogukan");
        MultiplicationResultAttempt attempt = new MultiplicationResultAttempt(user,multiplication,3000, false);
        MultiplicationResultAttempt verifiedAttempt = new MultiplicationResultAttempt(user,multiplication,3000, true);
        given(userRepository.findByAlias("Dogukan")).willReturn(Optional.empty());
        //WHEN
        boolean attemptResult = multiplicationServiceImpl.checkAttempt(attempt);

        //assert
        assertThat(attemptResult).isTrue();
        verify(multiplicationResultAttemptRepository).save(verifiedAttempt);
        verify(eventDispatcher).send(new MultiplicationSolvedEvent(verifiedAttempt.getId(),verifiedAttempt.getUser().getId(),verifiedAttempt.isCorrect()));
    }

    @Test
    public void checkWrongAttemptTest(){
        //GIVEN
        Multiplication multiplication = new Multiplication(50,60);
        User user = new User("Dogukan");
        MultiplicationResultAttempt attempt = new MultiplicationResultAttempt(user,multiplication,3001, false);
        given(userRepository.findByAlias("Dogukan")).willReturn(Optional.empty());
        //WHEN
        boolean attemptResult = multiplicationServiceImpl.checkAttempt(attempt);

        //assert
        assertThat(attemptResult).isFalse();
        verify(multiplicationResultAttemptRepository).save(attempt);
        verify(eventDispatcher).send(new MultiplicationSolvedEvent(attempt.getId(),attempt.getUser().getId(),attempt.isCorrect()));
    }

    @Test
    public void retrieveStatsTest(){
        Multiplication multiplication = new Multiplication(50,60);
        User user = new User("Dogukan");
        MultiplicationResultAttempt attempt1 = new MultiplicationResultAttempt(user,multiplication,3001, false);
        MultiplicationResultAttempt attempt2 = new MultiplicationResultAttempt(user,multiplication,3052, false);
        List<MultiplicationResultAttempt> attempts = Lists.newArrayList(attempt1,attempt2);
        given(userRepository.findByAlias("Dogukan")).willReturn(Optional.empty());
        given(multiplicationResultAttemptRepository.findTop5ByUserAliasOrderByIdDesc("Dogukan")).willReturn(attempts);

        List<MultiplicationResultAttempt> result = multiplicationServiceImpl.getStatsForUser("Dogukan");
        assertThat(attempts).isEqualTo(result);
    }

    @Test
    public void getResultAttemptTest(){
        Multiplication multiplication = new Multiplication(50,60);
        User user = new User("Dogukan");
        MultiplicationResultAttempt attempt = new MultiplicationResultAttempt(user,multiplication,3001, false);
        given(multiplicationResultAttemptRepository.findById(1L)).willReturn(Optional.of(attempt));

        MultiplicationResultAttempt result = multiplicationServiceImpl.getAttempt(1L);

        assertThat(attempt).isEqualTo(result);
    }
}
