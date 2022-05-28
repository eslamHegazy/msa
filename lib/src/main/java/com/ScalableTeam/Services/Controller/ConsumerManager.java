package com.ScalableTeam.Services.Controller;

import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistry;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ConsumerManager {
    private RabbitListenerEndpointRegistry rabbitListenerEndpointRegistry;

    public void changeMaxThreadCount(int maxThreadCount) {
        rabbitListenerEndpointRegistry.getListenerContainers()
                .stream().filter(container -> container instanceof SimpleMessageListenerContainer)
                .map(container -> (SimpleMessageListenerContainer) container)
                .forEach(container -> container.setMaxConcurrentConsumers(maxThreadCount));
    }

    public void changeMinThreadCount(int minThreadCount) {
        rabbitListenerEndpointRegistry.getListenerContainers()
                .stream().filter(container -> container instanceof SimpleMessageListenerContainer)
                .map(container -> (SimpleMessageListenerContainer) container)
                .forEach(container -> container.setConcurrentConsumers(minThreadCount));
    }

    public void stopAcceptingNewRequests() {
        rabbitListenerEndpointRegistry.stop();
    }

    public void startAcceptingNewRequests() {
        rabbitListenerEndpointRegistry.start();
    }
}
