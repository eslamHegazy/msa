package com.ScalableTeam.services.controllerserver;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ControllerRequestsServerConfig {
    @Bean("controllerServerBootstrap")
    public ServerBootstrap controllerServerBootstrap() {
        return new ServerBootstrap();
    }

    @Bean("controllerBootstrap")
    public Bootstrap bootstrap() {
        return new Bootstrap();
    }

    @Bean("controllerEventLoopGroup")
    public EventLoopGroup controllerEventLoopGroup() {
        return new NioEventLoopGroup(1);
    }

    @Bean("controllerWorkerEventLoopGroup")
    public EventLoopGroup controllerWorkerEventLoopGroup() {
        return new NioEventLoopGroup();
    }
}
