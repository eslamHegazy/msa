package com.ScalableTeam.services.controllerserver;

import com.ScalableTeam.services.managers.ControlManager;
import com.ScalableTeam.utils.StringUtils;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@ChannelHandler.Sharable
@RequiredArgsConstructor
@Component
@Slf4j
public class ControllerRequestsServerExecutor extends ChannelInboundHandlerAdapter {
    private final ControlManager controlManager;
    @Value("${spring.application.name: application}")
    private String appName;

    //    @Override
//    public void channelRead0(ChannelHandlerContext ctx, String command) throws Exception {
//        System.out.println(command.getBytes(StandardCharsets.UTF_8).length);
//        log.info("Channel for controller requests for app {} read command {}", appName, command);
//        controlManager.handleControllerMessage(new JSONObject(command));
//    }
//
//    @Override
//    public void channelReadComplete(ChannelHandlerContext ctx) {
//        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
//    }
//
//    @Override
//    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
//        cause.printStackTrace();
//        ctx.close();
//    }
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        ByteBuf buf = (ByteBuf) msg;
//        byte[] bytes;
//        int offset;
//        int length = buf.readableBytes();
//
//        if (buf.hasArray()) {
//            bytes = buf.array();
//            offset = buf.arrayOffset();
//        } else {
//            bytes = new byte[length];
//            buf.getBytes(buf.readerIndex(), bytes);
//            offset = 0;
//        }
//        StringBuilder sb = new StringBuilder();
//        for (int i = 0; i < bytes.length; i++) {
//            sb.append((char) bytes[i]);
//        }
        String command = StringUtils.byteBufferToString(msg);
        System.out.println(command.getBytes(StandardCharsets.UTF_8).length);
        System.out.println(command);
        log.info("Channel for controller requests for app {} read command {}", appName, command);
        controlManager.handleControllerMessage(new JSONObject(command));
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
