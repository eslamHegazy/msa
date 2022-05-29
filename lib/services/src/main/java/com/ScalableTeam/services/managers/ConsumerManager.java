package com.ScalableTeam.services.managers;

import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistry;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ConsumerManager {
    private final RabbitListenerEndpointRegistry rabbitListenerEndpointRegistry;

    public void changeMaxThreadCount(int maxThreadCount) {
        System.out.println(maxThreadCount);
        rabbitListenerEndpointRegistry.getListenerContainers()
                .stream().filter(container -> container instanceof SimpleMessageListenerContainer)
                .map(container -> (SimpleMessageListenerContainer) container)
                .forEach(container -> container.setMaxConcurrentConsumers(maxThreadCount));
        rabbitListenerEndpointRegistry.getListenerContainers().stream().forEach(messageListenerContainer -> System.out.println(messageListenerContainer));
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
