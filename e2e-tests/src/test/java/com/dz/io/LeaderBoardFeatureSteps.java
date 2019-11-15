package com.dz.io;

import com.dz.io.utils.model.LeaderBoardPosition;
import io.cucumber.java.en.Then;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

public class LeaderBoardFeatureSteps {
    private MultiplicationFeatureSteps mSteps;

    public LeaderBoardFeatureSteps(final MultiplicationFeatureSteps mSteps) {
        this.mSteps = mSteps;
    }

    @Then("the user ([^\\s]+) is number (\\d+) on the leaderboard")
    public void theUserDogukanIsNumberOnTheLeaderboard(final String user,
                                                       final int position) throws Throwable{
        Thread.currentThread().sleep(500);
        List<LeaderBoardPosition> leaderBoard = mSteps.getApp().getLeaderBoard();
        assertThat(leaderBoard).isNotEmpty();
        long userId = leaderBoard.get(position-1).getUserId();
        String userAlias = mSteps.getApp().getUser(userId).getAlias();
        assertThat(userAlias).isEqualTo(user);
    }
}
