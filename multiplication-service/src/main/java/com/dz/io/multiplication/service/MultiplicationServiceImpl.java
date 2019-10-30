package com.dz.io.multiplication.service;

import com.dz.io.multiplication.domain.Multiplication;
import com.dz.io.multiplication.domain.MultiplicationResultAttempt;
import com.dz.io.multiplication.domain.User;
import com.dz.io.multiplication.event.EventDispatcher;
import com.dz.io.multiplication.event.MultiplicationSolvedEvent;
import com.dz.io.multiplication.repository.MultiplicationRepository;
import com.dz.io.multiplication.repository.MultiplicationResultAttemptRepository;
import com.dz.io.multiplication.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;

@Service
public class MultiplicationServiceImpl implements MultiplicationService {

    private RandomGeneratorService randomGeneratorService;
    private MultiplicationResultAttemptRepository attemptRepository;
    private final MultiplicationRepository multiplicationRepository;
    private UserRepository userRepository;
    private EventDispatcher eventDispatcher;

    @Autowired
    public MultiplicationServiceImpl(final RandomGeneratorService randomGeneratorService,
                                     final MultiplicationResultAttemptRepository attemptRepository,
                                     final UserRepository userRepository,
                                     final MultiplicationRepository multiplicationRepository,
                                     final EventDispatcher eventDispatcher) {
        this.randomGeneratorService = randomGeneratorService;
        this.attemptRepository = attemptRepository;
        this.userRepository = userRepository;
        this.multiplicationRepository = multiplicationRepository;
        this.eventDispatcher = eventDispatcher;
    }

    @Override
    public Multiplication createRandomMultiplication() {
        int factorA = randomGeneratorService.generateRandomFactor();
        int factorB = randomGeneratorService.generateRandomFactor();
        return new Multiplication(factorA,factorB);
    }

    @Override
    public boolean checkAttempt(final MultiplicationResultAttempt resultAttempt) {
        Optional<User> user = userRepository.findByAlias(resultAttempt.getUser().getAlias());
        Optional<Multiplication>  multiplication =  multiplicationRepository.findByFactorAAndAndFactorB(resultAttempt.getMultiplication().getFactorA(), resultAttempt.getMultiplication().getFactorB());
        boolean correct = resultAttempt.getResultAttempt() == resultAttempt.getMultiplication().getFactorA() * resultAttempt.getMultiplication().getFactorB();
        Assert.isTrue(!resultAttempt.isCorrect(), "Not true");

        MultiplicationResultAttempt newResultAttempt = new MultiplicationResultAttempt(user.orElse(resultAttempt.getUser()),
                multiplication.orElse(resultAttempt.getMultiplication()),
                resultAttempt.getResultAttempt(),
                correct);
        attemptRepository.save(newResultAttempt);
        eventDispatcher.send(new MultiplicationSolvedEvent(newResultAttempt.getId(), newResultAttempt.getUser().getId(), newResultAttempt.isCorrect()));
        return correct;
    }

    @Override
    public List<MultiplicationResultAttempt> getStatsForUser(String alias) {
        return attemptRepository.findTop5ByUserAliasOrderByIdDesc(alias);
    }

    @Override
    public MultiplicationResultAttempt getAttempt(Long attempId) {
        return attemptRepository.findById(attempId).orElse(null);
    }

}
