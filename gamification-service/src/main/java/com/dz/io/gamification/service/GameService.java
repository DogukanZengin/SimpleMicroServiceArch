package com.dz.io.gamification.service;

import com.dz.io.gamification.domain.GameStats;

public interface GameService {

    GameStats newAttemptForUser(Long userId, Long attemptId, boolean correct);

    GameStats retrieveStatsForUser(Long userId);
}
