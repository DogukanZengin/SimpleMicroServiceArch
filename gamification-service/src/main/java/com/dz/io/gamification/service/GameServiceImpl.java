package com.dz.io.gamification.service;

import com.dz.io.gamification.client.MultiplicationResultAttemptClient;
import com.dz.io.gamification.client.dto.MultiplicationResultAttempt;
import com.dz.io.gamification.domain.Badge;
import com.dz.io.gamification.domain.BadgeCard;
import com.dz.io.gamification.domain.GameStats;
import com.dz.io.gamification.domain.ScoreCard;
import com.dz.io.gamification.repository.BadgeCardRepository;
import com.dz.io.gamification.repository.ScoreCardRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class GameServiceImpl implements GameService {

    private ScoreCardRepository scoreCardRepository;
    private BadgeCardRepository badgeCardRepository;
    private MultiplicationResultAttemptClient client;

    private static final int LUCKY_NUMBER = 42;
    @Autowired
    public GameServiceImpl(ScoreCardRepository scoreCardRepository, BadgeCardRepository badgeCardRepository,MultiplicationResultAttemptClient client) {
        this.scoreCardRepository = scoreCardRepository;
        this.badgeCardRepository = badgeCardRepository;
        this.client = client;
    }

    @Override
    public GameStats newAttemptForUser(Long userId, Long attemptId, boolean correct) {
        if(correct){
            ScoreCard scoreCard = new ScoreCard(userId, attemptId);
            scoreCardRepository.save(scoreCard);
            log.info("User with id {} scored points {} with the attempt {}",userId,scoreCard.getScore(),attemptId);
            List<BadgeCard> badgeCards = processBadges(userId, attemptId);
            return new GameStats(userId, scoreCard.getScore(), badgeCards.stream().map(BadgeCard::getBadge).collect(Collectors.toList()));
        }
        return GameStats.emptyStats(userId);
    }

    @Override
    public GameStats retrieveStatsForUser(final Long userId) {
        int score = scoreCardRepository.getTotalScoreForUser(userId);
        List<BadgeCard> badgeCards = badgeCardRepository.findByUserIdOrderByBadgeTimeStampDesc(userId);

        return new GameStats(userId, score, badgeCards.stream().map(BadgeCard::getBadge).collect(Collectors.toList()));
    }

    private List<BadgeCard> processBadges(final Long userId,
                                          final Long attemptId){
        List<BadgeCard> badgeCardList =  new ArrayList<>();
        int totalScore = scoreCardRepository.getTotalScoreForUser(userId);
        log.info("New score for user {} is {}",userId, totalScore);

        List<ScoreCard> scoreCards =  scoreCardRepository.findByUserIdOrderByScoreTimeStampDesc(userId);
        List<BadgeCard> badgeCards = badgeCardRepository.findByUserIdOrderByBadgeTimeStampDesc(userId);

        checkAndGiveBadgeBasedOnScore(badgeCards, Badge.BRONZE_MULTIPLICATOR,totalScore,100,userId).ifPresent(badgeCardList::add);
        checkAndGiveBadgeBasedOnScore(badgeCards, Badge.SILVER_MULTIPLICATOR,totalScore,500,userId).ifPresent(badgeCardList::add);
        checkAndGiveBadgeBasedOnScore(badgeCards, Badge.GOLD_MULTIPLICATOR,totalScore,999,userId).ifPresent(badgeCardList::add);
        MultiplicationResultAttempt attempt = client.retrieveMultiplicationResultAttemptById(attemptId);
        if(scoreCards.size() == 1 && !containsBadge(badgeCards, Badge.FIRST_WON)){
            badgeCardList.add(giveBadge(Badge.FIRST_WON,userId));
        }
        if(!containsBadge(badgeCards, Badge.LUCKY_NUMBER) && (attempt.getMultiplicationFactorA() == LUCKY_NUMBER || attempt.getMultiplicationFactorB() == LUCKY_NUMBER)){
            badgeCardList.add(giveBadge(Badge.LUCKY_NUMBER,userId));
        }
        return badgeCardList;
    }

    private Optional<BadgeCard> checkAndGiveBadgeBasedOnScore(final List<BadgeCard> badgeCards,
                                                              final Badge badge,
                                                              final int score,
                                                              final int scoreThreshold,
                                                              final Long userId){
        if(score >= scoreThreshold && containsBadge(badgeCards,badge)){
            Optional.of(giveBadge(badge, userId));
        }
        return Optional.empty();
    }

    private BadgeCard giveBadge(final Badge badge, final Long userId){
        BadgeCard badgeCard = new BadgeCard(userId, badge);
        badgeCardRepository.save(badgeCard);
        log.info("User with id {} won a new badge: {}",userId,badge);
        return badgeCard;
    }

    private boolean containsBadge(final List<BadgeCard> badgeList, Badge badge){
        return badgeList.stream().anyMatch(b -> b.getBadge().equals(badge));
    }
}
