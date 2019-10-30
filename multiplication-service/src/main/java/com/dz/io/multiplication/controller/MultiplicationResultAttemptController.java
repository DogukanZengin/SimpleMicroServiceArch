package com.dz.io.multiplication.controller;

import com.dz.io.multiplication.domain.MultiplicationResultAttempt;
import com.dz.io.multiplication.service.MultiplicationService;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/results")
public final class MultiplicationResultAttemptController {

    private final MultiplicationService multiplicationService;
    private final int serverPort;

    @Autowired
    public MultiplicationResultAttemptController(final MultiplicationService multiplicationService, @Value("${server.port}") int serverPort) {
        this.multiplicationService = multiplicationService;
        this.serverPort = serverPort;
    }

    @PostMapping
    ResponseEntity<MultiplicationResultAttempt> postResult(@RequestBody MultiplicationResultAttempt multiplicationResultAttempt){

        boolean correct = multiplicationService.checkAttempt(multiplicationResultAttempt);
        MultiplicationResultAttempt newResultAttempt = new MultiplicationResultAttempt(multiplicationResultAttempt.getUser(),
                multiplicationResultAttempt.getMultiplication(),
                multiplicationResultAttempt.getResultAttempt(),
                correct);
        return ResponseEntity.ok(newResultAttempt);
    }
    @GetMapping
    ResponseEntity<List<MultiplicationResultAttempt>> getStatistics(@RequestParam(name = "alias") String alias){
        return ResponseEntity.ok(multiplicationService.getStatsForUser(alias));
    }

    @GetMapping("/{resultId}")
    ResponseEntity<MultiplicationResultAttempt> getResultAttempt(final @PathVariable(name = "resultId") Long resultId){
        log.info("Retrieving result from server @ {}",serverPort);
        return ResponseEntity.ok(multiplicationService.getAttempt(resultId));
    }

}
