package com.dz.io.gamification.repository;

import com.dz.io.gamification.domain.LeaderBoardRow;
import com.dz.io.gamification.domain.ScoreCard;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ScoreCardRepository extends CrudRepository<ScoreCard, Long> {

    @Query("SELECT sum(s.score) FROM com.dz.io.gamification.domain.ScoreCard s WHERE s.userId = :userId GROUP BY s.userId")
    int getTotalScoreForUser(@Param("userId") final Long userId);

    @Query("SELECT NEW com.dz.io.gamification.domain.LeaderBoardRow(s.userId, SUM(s.score))" +
            " FROM com.dz.io.gamification.domain.ScoreCard s GROUP BY s.userId ORDER BY SUM(s.score) DESC")
    List<LeaderBoardRow> getFirst10();

    List<ScoreCard> findByUserIdOrderByScoreTimeStampDesc(final Long userId);
}
