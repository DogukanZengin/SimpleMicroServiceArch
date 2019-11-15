package com.dz.io.utils.model;

public class ScoreResponse {

    private long userId;
    private int score;

    public ScoreResponse(final int score) {
        this.score = score;
    }

    public long getUserId() {
        return userId;
    }

    public int getScore() {
        return score;
    }
}
