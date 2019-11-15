package com.dz.io.utils.model;

public class AttemptResponse {
    private boolean correct;
    private long id;
    private User user;

    public AttemptResponse() {
    }

    public boolean isCorrect() {
        return correct;
    }

    public long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }
}
