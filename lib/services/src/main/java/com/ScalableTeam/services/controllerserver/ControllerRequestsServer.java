package com.ScalableTeam.services.controllerserver;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.net.UnknownHostException;

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
//                    .option(ChannelOption.SO_RCVBUF, 1048576)
//                    .childOption(ChannelOption.SO_RCVBUF, 1048576)
                    .localAddress(tcpPort)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) {
                            ch.pipeline().addLast(new DelimiterBasedFrameDecoder(Integer.MAX_VALUE, Unpooled.wrappedBuffer(new byte[]{'E', 'O', 'F', '\n'})));
                            ch.pipeline().addLast(controllerRequestsServerExecutor);//(3)
                        }
                    });

            ChannelFuture f = serverBootstrap.bind().sync();
            log.info("{} started and listening on {} with send buffer {} and receive buffer {}", ControllerRequestsServer.class.getSimpleName(),
                    f.channel().localAddress(),
                    f.channel().config().getOption(ChannelOption.SO_SNDBUF),
                    f.channel().config().getOption(ChannelOption.SO_RCVBUF));
            f.channel().closeFuture().sync();
        } finally {
            eventLoopGroup.shutdownGracefully().sync(); // Terminates all threads
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
