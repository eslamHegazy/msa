package com.ScalableTeam.services.managers;

import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistry;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

@Component
@AllArgsConstructor
public class ConsumerManager {
    private final RabbitListenerEndpointRegistry rabbitListenerEndpointRegistry;

    public void changeMaxThreadCount(int maxThreadCount) {
        getMessageContainers()
                .forEach(container -> container.setMaxConcurrentConsumers(maxThreadCount));
    }

    public void changeMinThreadCount(int minThreadCount) {
        getMessageContainers()
                .forEach(container -> container.setConcurrentConsumers(minThreadCount));
    }

    private Stream<SimpleMessageListenerContainer> getMessageContainers() {
        return rabbitListenerEndpointRegistry.getListenerContainers()
                .stream().filter(container -> container instanceof SimpleMessageListenerContainer)
                .map(container -> (SimpleMessageListenerContainer) container);
    }

    public void stopAcceptingNewRequests() {
        rabbitListenerEndpointRegistry.stop();
    }

    public void startAcceptingNewRequests() {
        rabbitListenerEndpointRegistry.start();
    }

}
