package com.ScalableTeam.controller.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
                    .group(eventLoopGroup)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(tcpPort)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(serverInitializer);
                        }
                    });

            ChannelFuture f = serverBootstrap.bind().sync();
            log.info("{} started and listening on {} ", ControllerServer.class.getSimpleName(), f.channel().localAddress());
            f.channel().closeFuture().sync();
        } finally {
            eventLoopGroup.shutdownGracefully().sync(); // Terminates all threads
        }
    }

    public void setTcpPort(int port) {
        this.tcpPort = new InetSocketAddress(port);
    }

    public InetSocketAddress getTcpPort() {
        return tcpPort;
    }
}