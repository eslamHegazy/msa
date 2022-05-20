package com.ScalableTeam.amqp;

import lombok.AllArgsConstructor;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@AllArgsConstructor
public class RabbitMQBinders {
    private final Config config;
    private final ConfigurableListableBeanFactory beanFactory;

    @Bean
    Map<String, Queue> queues() throws InvocationTargetException, IllegalAccessException {
        Map<String, Queue> queues = new HashMap<>();
        Config.Queues.Request requests = config.getQueues().getRequest();
        Config.Queues.Response responses = config.getQueues().getResponse();
        Class<?> requestsClass = requests.getClass();
        Class<?> responsesClass = responses.getClass();

        Field[] microservices = requestsClass.getDeclaredFields();
        Method m;
        Map<String, String> commands;
        for (Field micro : microservices) {
            String microservice = micro.getName();
            String methodName = "get" + microservice.substring(0, 1).toUpperCase() + microservice.substring(1);
            try {
                m = requestsClass.getMethod(methodName);
                commands = (Map<String, String>) m.invoke(requests);
                for (String key : commands.keySet()) {
                    String queueName = commands.get(key);
                    queues.put(queueName, createQueue(queueName));
                }
            } catch (NoSuchMethodException | SecurityException ignored) {

            }
        }
        microservices = responsesClass.getDeclaredFields();
        for (Field micro : microservices) {
            String microservice = micro.getName();
            String methodName = "get" + microservice.substring(0, 1).toUpperCase() + microservice.substring(1);
            try {
                m = responsesClass.getMethod(methodName);
                commands = (Map<String, String>) m.invoke(responses);
                for (String key : commands.keySet()) {
                    String queueName = commands.get(key);
                    queues.put(queueName, createQueue(queueName));
                }
            } catch (NoSuchMethodException | SecurityException ignored) {

            }
        }
        System.out.println(queues);
        return queues;
    }

    private Queue createQueue(String queueName) {
        Queue q = new Queue(queueName, true);
        this.beanFactory.initializeBean(q, queueName);
        this.beanFactory.autowireBean(q);
        this.beanFactory.registerSingleton(queueName, q);
        return q;
    }

    @Bean
    DirectExchange exchange() {
        return new DirectExchange(config.getExchange());
    }

    @Bean
    List<Binding> bindings(Map<String, Queue> queues, DirectExchange exchange) {
        List<Binding> bindings = new ArrayList<>();
        for (String key : queues.keySet()) {
            bindings.add(binding(queues, key, exchange));
        }
        return bindings;
    }

    private Binding binding(Map<String, Queue> queues, String key, DirectExchange exchange) {
        Binding b = BindingBuilder.bind(queues.get(key)).to(exchange).withQueueName();
        this.beanFactory.initializeBean(b, key + "Binder");
        this.beanFactory.autowireBean(b);
        this.beanFactory.registerSingleton(key + "Binder", b);
        return b;
    }
}