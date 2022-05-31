package com.ScalableTeam.services.controllerserver;

import com.ScalableTeam.services.managers.ControlManager;
import com.ScalableTeam.utils.StringUtils;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.boot.context.ContextIdApplicationContextInitializer;
import org.springframework.stereotype.Component;

@ChannelHandler.Sharable
@RequiredArgsConstructor
@Component
@Slf4j
public class ControllerRequestsServerExecutor extends ChannelInboundHandlerAdapter {
    private final ControlManager controlManager;
    @Value("${spring.application.name: application}")
    private String appName;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String command = StringUtils.byteBufferToString(msg);
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
