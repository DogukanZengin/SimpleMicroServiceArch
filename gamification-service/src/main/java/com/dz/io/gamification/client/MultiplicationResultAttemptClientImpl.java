package com.dz.io.gamification.client;

import com.dz.io.gamification.client.dto.MultiplicationResultAttempt;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class MultiplicationResultAttemptClientImpl implements MultiplicationResultAttemptClient {

    private final RestTemplate restTemplate;
    private final String multiplicationHost;

    @Autowired
    public MultiplicationResultAttemptClientImpl(RestTemplate restTemplate, @Value("${multiplication.host}") String multiplicationHost) {
        this.restTemplate = restTemplate;
        this.multiplicationHost = multiplicationHost;
    }

    @HystrixCommand(fallbackMethod = "defaultResult")
    @Override
    public MultiplicationResultAttempt retrieveMultiplicationResultAttemptById(Long multiplicationId) {
        return restTemplate.getForObject(multiplicationHost + "results/" + multiplicationId, MultiplicationResultAttempt.class);
    }

    private MultiplicationResultAttempt defaultResult(final Long multiplicationResultAttemptId){
        return new MultiplicationResultAttempt("fallback",10,10,100,true);
    }
}
