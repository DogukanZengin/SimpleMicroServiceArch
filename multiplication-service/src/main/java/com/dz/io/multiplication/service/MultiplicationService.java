package com.dz.io.multiplication.service;

import com.dz.io.multiplication.domain.Multiplication;
import com.dz.io.multiplication.domain.MultiplicationResultAttempt;

import java.util.List;
import java.util.Optional;

public interface MultiplicationService {

    /**
     *
     * @return
     */
    Multiplication createRandomMultiplication();
    boolean checkAttempt(final MultiplicationResultAttempt resultAttempt);
    List<MultiplicationResultAttempt> getStatsForUser(String alias);
    MultiplicationResultAttempt getAttempt(Long attempId);
}
