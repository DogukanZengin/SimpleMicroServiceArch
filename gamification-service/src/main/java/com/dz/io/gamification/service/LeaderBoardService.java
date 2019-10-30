package com.dz.io.gamification.service;

import com.dz.io.gamification.domain.LeaderBoardRow;

import java.util.List;

public interface LeaderBoardService {

    List<LeaderBoardRow> getCurrentLeaderBoard();
}
