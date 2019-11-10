package com.dz.io.gamification.service;

import com.dz.io.gamification.client.MultiplicationResultAttemptClient;
import com.dz.io.gamification.client.dto.MultiplicationResultAttempt;
import com.dz.io.gamification.domain.Badge;
import com.dz.io.gamification.domain.BadgeCard;
import com.dz.io.gamification.domain.GameStats;
import com.dz.io.gamification.domain.ScoreCard;
import com.dz.io.gamification.repository.BadgeCardRepository;
import com.dz.io.gamification.repository.ScoreCardRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

public class GameServiceImplTest {

    @Mock
    private ScoreCardRepository scoreCardRepository;

    @Mock
    private BadgeCardRepository badgeCardRepository;

    @Mock
    private MultiplicationResultAttemptClient multiplicationResultAttemptClient;

    private GameServiceImpl gameService;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        gameService =  new GameServiceImpl(scoreCardRepository, badgeCardRepository, multiplicationResultAttemptClient);
        MultiplicationResultAttempt attempt = new MultiplicationResultAttempt("Dogukan",30,40,1200,true);
        given(multiplicationResultAttemptClient.retrieveMultiplicationResultAttemptById(anyLong())).willReturn(attempt);
    }

    @Test
    public void whenFirstCorrectAttempt_ThenFirstWonBadge(){
        Long userId = 1L;
        Long attemptId = 2L;
        int score = 10;
        ScoreCard scoreCard =  new ScoreCard(userId, attemptId);
        given(scoreCardRepository.getTotalScoreForUser(userId)).willReturn(score);
        given(scoreCardRepository.findByUserIdOrderByScoreTimeStampDesc(userId)).willReturn(Collections.singletonList(scoreCard));
        given(badgeCardRepository.findByUserIdOrderByBadgeTimeStampDesc(userId)).willReturn(Collections.emptyList());

        GameStats gameStats = gameService.newAttemptForUser(userId, attemptId, true);

        assertThat(gameStats.getScore()).isEqualTo(score);
        assertThat(gameStats.getBadges()).containsOnly(Badge.FIRST_WON);
    }

    @Test
    public void whenWrongAttempt_ThenReturnStats(){
        Long userId = 1L;
        Long attemptId = 2L;
        int score = 10;
        ScoreCard scoreCard =  new ScoreCard(userId, attemptId);

        given(scoreCardRepository.getTotalScoreForUser(userId)).willReturn(score);
        given(scoreCardRepository.findByUserIdOrderByScoreTimeStampDesc(userId)).willReturn(Collections.singletonList(scoreCard));
        given(badgeCardRepository.findByUserIdOrderByBadgeTimeStampDesc(userId)).willReturn(Collections.emptyList());

        GameStats gameStats = gameService.newAttemptForUser(userId, attemptId, false);

        assertThat(gameStats.getScore()).isEqualTo(0);
        assertThat(gameStats.getBadges()).isEmpty();

    }

    @Test
    public void whenLuckyNumber_GiveBadge(){
        Long userId = 1L;
        Long attemptId = 30L;
        int totalScore = 10;
        BadgeCard badgeCard = new BadgeCard(userId, Badge.FIRST_WON);
        given(scoreCardRepository.getTotalScoreForUser(userId)).willReturn(totalScore);
        given(scoreCardRepository.findByUserIdOrderByScoreTimeStampDesc(userId)).willReturn(createNScoreCards(1, userId));
        given(badgeCardRepository.findByUserIdOrderByBadgeTimeStampDesc(userId)).willReturn(Collections.singletonList(badgeCard));

        MultiplicationResultAttempt attempt = new MultiplicationResultAttempt("Dogukan",42,30,1260,true);
        given(multiplicationResultAttemptClient.retrieveMultiplicationResultAttemptById(attemptId)).willReturn(attempt);
        GameStats stats =  gameService.newAttemptForUser(userId,attemptId,true);

        assertThat(stats.getScore()).isEqualTo(ScoreCard.DEFAULT_SCORE);
        assertThat(stats.getBadges()).containsOnly(Badge.LUCKY_NUMBER);
    }

    @Test
    public void getScoreForAttemptTest(){
        Long userId = 1L;
        Long attemptId = 30L;
        ScoreCard scoreCard =  new ScoreCard(userId, attemptId);
        given(scoreCardRepository.findByAttemptId(attemptId)).willReturn(scoreCard);

        ScoreCard score = gameService.getScoreForAttempt(attemptId);

        assertThat(score.getUserId()).isEqualTo(userId);
        assertThat(score.getAttemptId()).isEqualTo(attemptId);
    }

    private List<ScoreCard> createNScoreCards(int n, Long userId){
        return IntStream.range(0,n).mapToObj(i -> new ScoreCard(userId,(long)i)).collect(Collectors.toList());
    }
}
