package com.dz.io.gamification.event;


import com.dz.io.gamification.service.GameService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class EventHandler {

    private GameService gameService;

    @Autowired
    public EventHandler(final GameService gameService) {
        this.gameService = gameService;
    }

    @RabbitListener(queues = "${multiplication.queue}")
    void handleMultiplicationSolved(final MultiplicationSolvedEvent event){
        log.info("Multiplication solved event received : {}",event.getMultiplicationResultAttemptId());

        try{
            gameService.newAttemptForUser(event.getUserId(), event.getMultiplicationResultAttemptId(), event.isCorrect());
        }catch (final Exception e){
            log.error("Error when trying to process event",e);
            throw new AmqpRejectAndDontRequeueException(e);
        }
    }
}
