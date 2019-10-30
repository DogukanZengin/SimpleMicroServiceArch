package com.dz.io.gamification.service;

import com.dz.io.gamification.domain.LeaderBoardRow;
import com.dz.io.gamification.domain.ScoreCard;
import com.dz.io.gamification.repository.ScoreCardRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.BDDMockito.given;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LeaderBoardServiceImplTest {


    private LeaderBoardServiceImpl leaderBoardService;

    @Mock
    private ScoreCardRepository scoreCardRepository;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        leaderBoardService =  new LeaderBoardServiceImpl(scoreCardRepository);
    }

    @Test
    public void whenLeaderBoardRequested_ThenReturnCurrentLeaderBoard(){

        List<LeaderBoardRow> leaderBoard = createLeaderBoardRows();
        given(scoreCardRepository.getFirst10()).willReturn(leaderBoard);
        Assert.assertEquals(leaderBoard, leaderBoardService.getCurrentLeaderBoard());
    }

    private List<LeaderBoardRow> createLeaderBoardRows(){
        return Stream.generate(() -> new LeaderBoardRow(1L,10L)).limit(10).collect(Collectors.toList());
    }
}

