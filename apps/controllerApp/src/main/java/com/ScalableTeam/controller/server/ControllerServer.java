package com.ScalableTeam.controller.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;

@Component
@Slf4j
public class ControllerServer {
    private final ServerBootstrap serverBootstrap;
    private final EventLoopGroup eventLoopGroup;
    private final EventLoopGroup eventWorkerLoopGroup;
    private final ControllerServerExecutor controllerServerExecutor;
    private final ServerInitializer serverInitializer;

    private InetSocketAddress tcpPort;

    public ControllerServer(@Qualifier("controllerServerBootstrap") ServerBootstrap serverBootstrap,
                            @Qualifier("controllerEventLoopGroup")
                                    EventLoopGroup eventLoopGroup,
                            @Qualifier("controllerWorkerEventLoopGroup")
                                    EventLoopGroup eventWorkerLoopGroup,
                            ControllerServerExecutor controllerServerExecutor,
                            ServerInitializer serverInitializer
    ) {
        this.serverBootstrap = serverBootstrap;
        this.eventLoopGroup = eventLoopGroup;
        this.eventWorkerLoopGroup = eventWorkerLoopGroup;
        this.controllerServerExecutor = controllerServerExecutor;
        this.serverInitializer = serverInitializer;
    }

    public void start() throws InterruptedException {
        try {
            serverBootstrap
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .group(eventLoopGroup, eventWorkerLoopGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(serverInitializer);

            Channel f = serverBootstrap.bind(tcpPort).sync().channel();
            log.info("{} started and listening on {} ", ControllerServer.class.getSimpleName(), f.localAddress());
            f.closeFuture().sync();
        } finally {
            eventLoopGroup.shutdownGracefully().sync(); // Terminates all threads
            eventWorkerLoopGroup.shutdownGracefully().sync(); // Terminates all threads
        }
    }

    public void setTcpPort(int port) {
        this.tcpPort = new InetSocketAddress(port);
    }

    public InetSocketAddress getTcpPort() {
        return tcpPort;
    }
}