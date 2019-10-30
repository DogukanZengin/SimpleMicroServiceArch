package com.dz.io.gamification.event;

import com.dz.io.gamification.domain.GameStats;
import com.dz.io.gamification.service.GameService;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class EventHandLerTest {

    private EventHandler eventHandler;

    @Mock
    private GameService gameService;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        eventHandler = new EventHandler(gameService);
    }

    @Test
    public void multiplicationAttemptReceivedTest(){
        Long userId = 1L;
        Long attemptId = 4L;
        boolean correct = true;
        GameStats expected =  new GameStats();
        MultiplicationSolvedEvent event =  new MultiplicationSolvedEvent(attemptId,userId,correct);

        given(gameService.newAttemptForUser(userId,attemptId,correct)).willReturn(expected);

        eventHandler.handleMultiplicationSolved(event);

        verify(gameService).newAttemptForUser(userId,attemptId,correct);

    }
}
