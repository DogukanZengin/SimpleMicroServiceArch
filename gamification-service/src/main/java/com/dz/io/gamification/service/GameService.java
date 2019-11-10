package com.dz.io.gamification.service;

import com.dz.io.gamification.domain.GameStats;
import com.dz.io.gamification.domain.ScoreCard;

public interface GameService {

    GameStats newAttemptForUser(Long userId, Long attemptId, boolean correct);

    GameStats retrieveStatsForUser(Long userId);

    ScoreCard getScoreForAttempt(Long attemptId);
}
