package com.dz.io.gamification.service;

import com.dz.io.gamification.repository.BadgeCardRepository;
import com.dz.io.gamification.repository.ScoreCardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("test")
public class AdminServiceImpl implements AdminService {

    private final BadgeCardRepository badgeCardRepository;
    private final ScoreCardRepository scoreCardRepository;

    @Autowired
    public AdminServiceImpl(final BadgeCardRepository badgeCardRepository,
                            final ScoreCardRepository scoreCardRepository) {
        this.badgeCardRepository = badgeCardRepository;
        this.scoreCardRepository = scoreCardRepository;
    }

    @Override
    public void deleteDatabaseContents() {
        badgeCardRepository.deleteAll();
        scoreCardRepository.deleteAll();
    }
}
