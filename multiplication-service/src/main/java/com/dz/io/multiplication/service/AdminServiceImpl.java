package com.dz.io.multiplication.service;

import com.dz.io.multiplication.repository.MultiplicationRepository;
import com.dz.io.multiplication.repository.MultiplicationResultAttemptRepository;
import com.dz.io.multiplication.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("test")
public class AdminServiceImpl implements AdminService {

    private final MultiplicationResultAttemptRepository multiplicationResultAttemptRepository;
    private final MultiplicationRepository multiplicationRepository;
    private final UserRepository userRepository;

    @Autowired
    public AdminServiceImpl(final MultiplicationResultAttemptRepository multiplicationResultAttemptRepository,
                            final MultiplicationRepository multiplicationRepository,
                            final UserRepository userRepository) {
        this.multiplicationResultAttemptRepository = multiplicationResultAttemptRepository;
        this.multiplicationRepository = multiplicationRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void deleteDatabaseContents() {
        multiplicationRepository.deleteAll();
        multiplicationResultAttemptRepository.deleteAll();
        userRepository.deleteAll();
    }
}
