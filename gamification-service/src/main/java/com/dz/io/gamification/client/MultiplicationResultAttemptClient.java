package com.dz.io.gamification.client;

import com.dz.io.gamification.client.dto.MultiplicationResultAttempt;

public interface MultiplicationResultAttemptClient {
    MultiplicationResultAttempt retrieveMultiplicationResultAttemptById(final Long multiplicationId);
}
