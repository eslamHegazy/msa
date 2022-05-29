package com.ScalableTeam.services.controllerserver;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;

@Component
@Slf4j
public class ControllerRequestsServer {
    private final ServerBootstrap serverBootstrap;
    private final EventLoopGroup eventLoopGroup;
    private final ControllerRequestsServerExecutor controllerRequestsServerExecutor;

    private InetSocketAddress tcpPort;

    public ControllerRequestsServer(@Qualifier("controllerServerBootstrap") ServerBootstrap serverBootstrap,
                            @Qualifier("controllerEventLoopGroup")
                                    EventLoopGroup eventLoopGroup,
                            @Qualifier("controllerWorkerEventLoopGroup")
                                    EventLoopGroup eventWorkerLoopGroup,
                            ControllerRequestsServerExecutor controllerRequestsServerExecutor
    ) {
        this.serverBootstrap = serverBootstrap;
        this.eventLoopGroup = eventLoopGroup;
        this.controllerRequestsServerExecutor = controllerRequestsServerExecutor;
    }

    public void start() throws InterruptedException {
        try {
            serverBootstrap
                    .group(eventLoopGroup)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(tcpPort)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(controllerRequestsServerExecutor);
                        }
                    });

            ChannelFuture f = serverBootstrap.bind().sync();
            log.info("{} started and listening on {} ", ControllerRequestsServer.class.getSimpleName(), f.channel().localAddress());
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
