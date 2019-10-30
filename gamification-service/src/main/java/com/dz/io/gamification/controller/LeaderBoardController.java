package com.dz.io.gamification.controller;

import com.dz.io.gamification.domain.LeaderBoardRow;
import com.dz.io.gamification.service.LeaderBoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/leaders")
public class LeaderBoardController {

    private LeaderBoardService leaderBoardService;

    @Autowired
    public LeaderBoardController(LeaderBoardService leaderBoardService) {
        this.leaderBoardService = leaderBoardService;
    }

    @GetMapping
    public List<LeaderBoardRow> getLeaderBoard(){
        return leaderBoardService.getCurrentLeaderBoard();
    }
}
