package com.ScalableTeam.controller.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.net.UnknownHostException;

@Component
@Slf4j
public class ControllerClient {
    private final Bootstrap bootstrap;
    private final EventLoopGroup eventWorkerEventLoopGroup;
    private final ControllerClientExecutor controllerClientExecutor;

    private InetSocketAddress tcpPort;

    public ControllerClient(@Qualifier("controllerBootstrap") Bootstrap bootstrap,
                            @Qualifier("controllerWorkerEventLoopGroup")
                                    EventLoopGroup eventWorkerEventLoopGroup,
                            ControllerClientExecutor controllerClientExecutor
    ) {
        this.bootstrap = bootstrap;
        this.eventWorkerEventLoopGroup = eventWorkerEventLoopGroup;
        this.controllerClientExecutor = controllerClientExecutor;
    }

    public void start() throws InterruptedException {
        try {
            bootstrap
                    .group(eventWorkerEventLoopGroup)
                    .channel(NioSocketChannel.class)
                    .remoteAddress(tcpPort)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) {
                            ch.pipeline().addLast(controllerClientExecutor);
                        }
                    });

            ChannelFuture f = bootstrap.connect().sync();
            log.info("{} started and listening on {} ", ControllerClient.class.getSimpleName(), f.channel().localAddress());
            f.channel().closeFuture().sync();
        } finally {
            eventWorkerEventLoopGroup.shutdownGracefully().sync(); // Terminates all threads
        }
    }

    public void setTcpPort(int port) {
        this.tcpPort = new InetSocketAddress(port);
    }

    public void setTcp(String ip, int port) throws UnknownHostException {
        this.tcpPort = new InetSocketAddress(ip, port);
    }

    public InetSocketAddress getTcpPort() {
        return tcpPort;
    }
}
