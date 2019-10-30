package com.dz.io.multiplication.service;

import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class RandomGeneratorServiceImpl implements RandomGeneratorService {

    final static int MAXIMUM_FACTOR = 99;
    final static int MINIMUM_FACTOR = 11;
    @Override
    public int generateRandomFactor() {
        return new Random().nextInt(MAXIMUM_FACTOR - MINIMUM_FACTOR + 1) + MINIMUM_FACTOR;
    }
}
