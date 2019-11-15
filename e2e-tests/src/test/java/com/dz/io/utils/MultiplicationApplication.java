package com.dz.io.utils;

import com.dz.io.utils.http.HttpUtils;
import com.dz.io.utils.model.*;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

public class MultiplicationApplication {

    private static final String APPLICATION_BASE_URL = "http://localhost:8000/api";
    private static final String CONTEXT_ATTEMPTS = "/results";
    private static final String CONTEXT_SCORE = "/scores/";
    private static final String CONTEXT_STATS = "/stats";
    private static final String CONTEXT_USERS = "/users/";
    private static final String CONTEXT_LEADERBOARD = "/leaders";
    private static final String CONTEXT_DELETE_GAME = "/gamification/admin/delete-db";
    private static final String CONTEXT_DELETE_MULT = "/multiplication/admin/delete-db";

    private HttpUtils httpUtils;

    public MultiplicationApplication() {
        this.httpUtils = new HttpUtils(APPLICATION_BASE_URL);
    }

    public AttemptResponse sendAttempt(String userAlias,
                                       int factorA,
                                       int factorB,
                                       int result){

        String attemptJson = "{\"user\":{\"alias\":\"" + userAlias + "\"}," +
                "\"multiplication\":{\"factorA\":\""
                + factorA + "\",\"factorB\":\""
                + factorB + "\"}," + "\"resultAttempt\":\"" + result + "\"}";
        String response =  httpUtils.post(CONTEXT_ATTEMPTS,attemptJson);
        ObjectMapper mapper =  new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
        try {
            return mapper.readValue(response,AttemptResponse.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ScoreResponse getScoreForAttempt(long attemptId){
        String response =  httpUtils.get(CONTEXT_SCORE + attemptId);
        if(response.isEmpty()){
            return new ScoreResponse(0);
        }else{
            ObjectMapper mapper =  new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
            try {
                return mapper.readValue(response,ScoreResponse.class);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public Stats getStatsForUser(long userId){
        String response =  httpUtils.get(CONTEXT_USERS + "?userId" + userId);
        ObjectMapper mapper =  new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
        try {
            return mapper.readValue(response,Stats.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public User getUser(long userId){
        String response =  httpUtils.get(CONTEXT_USERS + userId);
        ObjectMapper mapper =  new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
        try {
            return mapper.readValue(response,User.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<LeaderBoardPosition> getLeaderBoard(){
        String response =  httpUtils.get(CONTEXT_LEADERBOARD);
        ObjectMapper mapper =  new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
        try{
            JavaType javaType = mapper.getTypeFactory().constructCollectionType(List.class, LeaderBoardPosition.class);
            return mapper.readValue(response, javaType);
        }catch(IOException e){
            throw new RuntimeException(e);
        }
    }

    public void deleteData(){
        httpUtils.post(CONTEXT_DELETE_GAME,"");
        httpUtils.post(CONTEXT_DELETE_MULT,"");
    }
}
