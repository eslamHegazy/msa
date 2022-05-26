package com.ScalableTeam.amqp;

import java.util.concurrent.CountDownLatch;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RabbitMQReceiver {

    private CountDownLatch latch = new CountDownLatch(1);

    public void receiveMessage(String message) {
        log.info("Received < {message} >", message);
        latch.countDown();
    }

    public CountDownLatch getLatch() {
        return latch;
    }

}
