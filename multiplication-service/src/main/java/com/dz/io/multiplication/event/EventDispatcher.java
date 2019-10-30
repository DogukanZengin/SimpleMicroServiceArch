package com.dz.io.multiplication.event;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class EventDispatcher {

    private RabbitTemplate rabbitTemplate;
    private String exchange;
    private String routingKey;

    @Autowired
    public EventDispatcher(final RabbitTemplate rabbitTemplate,
                           @Value("${multiplication.exchange}") final String exchange,
                           @Value("${multiplication.solved.key}") final String routingKey) {
        this.rabbitTemplate = rabbitTemplate;
        this.exchange = exchange;
        this.routingKey = routingKey;
    }

    public void send(final MultiplicationSolvedEvent event){
        rabbitTemplate.convertAndSend(exchange, routingKey, event);
    }
}
