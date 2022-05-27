package com.ScalableTeam.notifications.di;

import com.ScalableTeam.notifications.data.FakeNotificationsRepository;
import com.ScalableTeam.notifications.data.NotificationsRepository;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestBeansConfig {

    @Bean
    public NotificationsRepository notificationsRepository() {
        return new FakeNotificationsRepository();
    }
}
