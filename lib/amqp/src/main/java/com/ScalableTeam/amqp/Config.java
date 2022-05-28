package com.ScalableTeam.amqp;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@ConfigurationProperties(prefix = "mq")
@Data
public class Config {
    private Queues queues;
    private String exchange;
    private Exceptions exceptions;

    @ConfigurationProperties(prefix = "exceptions")
    @Data
    public static class Exceptions {
        private String exchange;
        private String queue;
    }

    @ConfigurationProperties(prefix = "queues")
    @Data
    public static class Queues {
        private Request request;
        private Response response;

        @ConfigurationProperties(prefix = "request")
        @Data
        public static class Request {
            private Map<String, String> reddit;
            private Map<String, String> notifications;
        }

        @ConfigurationProperties(prefix = "response")
        @Data
        public static class Response {
            private Map<String, String> reddit;
            private Map<String, String> notifications;
        }
    }
}
