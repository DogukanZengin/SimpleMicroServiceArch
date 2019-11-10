package com.dz.io.gamification.controller;

import com.dz.io.gamification.domain.ScoreCard;
import com.dz.io.gamification.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/scores")
public class ScoreController {

    private final GameService gameService;

    @Autowired
    public ScoreController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping("/{attempId}")
    public ScoreCard getScoreForAttempt(@PathVariable("attempId") final Long attempId){
        return gameService.getScoreForAttempt(attempId);
    }
}
