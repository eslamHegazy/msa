package com.ScalableTeam.notifications;

import com.ScalableTeam.amqp.Config;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class NotificationConsumer {

    @RabbitListener(queues = "${mq.queues.request.notifications.publishNotification}")
    public void onMessage(Message message) throws JSONException {
        JSONObject obj = new JSONObject(new String(message.getBody()));
        log.info("Response message from-me: {} {}", obj.getString("name"), obj.getInt("age"));
    }
}