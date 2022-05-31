package com.ScalableTeam.controller.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@ChannelHandler.Sharable
@Slf4j
@Component
@NoArgsConstructor
@Data
public class ControllerClientExecutor extends SimpleChannelInboundHandler<ByteBuf> {
    private String command;

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        log.info("Channel {} is active\n", ctx.channel().id());
        ctx.writeAndFlush(Unpooled.copiedBuffer(command, CharsetUtil.UTF_8));
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, ByteBuf in) {
        throw new UnsupportedOperationException("Controller Client shouldn't receive anything");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
