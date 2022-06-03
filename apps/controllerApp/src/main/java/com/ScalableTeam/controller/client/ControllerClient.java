package com.ScalableTeam.controller.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.net.UnknownHostException;

@Component
@Slf4j
public class ControllerClient {
    private final ControllerClientExecutor controllerClientExecutor;

    private InetSocketAddress tcpPort;

    public ControllerClient(ControllerClientExecutor controllerClientExecutor
    ) {
        this.controllerClientExecutor = controllerClientExecutor;
    }

    public void start() throws InterruptedException {
        EventLoopGroup e = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap
                    .group(e)
                    .channel(NioSocketChannel.class)
//                    .option(ChannelOption.SO_SNDBUF, 1048576)
//                    .option(ChannelOption.SO_RCVBUF, 1048576)
                    .remoteAddress(tcpPort)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) {
                            ch.pipeline().addLast(controllerClientExecutor);
                        }
                    });
            try {
                ChannelFuture f = bootstrap.connect().sync();
                log.info("{} started and listening on {} and remote address {} and with send buffer {} and receive buffer {}", ControllerClient.class.getSimpleName(),
                        f.channel().localAddress(),
                        f.channel().remoteAddress(),
                        f.channel().config().getOption(ChannelOption.SO_SNDBUF),
                        f.channel().config().getOption(ChannelOption.SO_RCVBUF));
                f.channel().closeFuture().sync();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        } finally {
            e.shutdownGracefully().sync();
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
