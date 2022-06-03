package com.ScalableTeam.controller.server;


import com.ScalableTeam.controller.ControllerPublisher;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@ChannelHandler.Sharable
@AllArgsConstructor
@Component
@Slf4j
public class ControllerServerExecutor extends SimpleChannelInboundHandler<HttpObject> {
    private final ControllerPublisher controllerPublisher;

    @Override
    public void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
        String command = "";
        if (msg instanceof HttpContent) {
            HttpContent content = (HttpContent) msg;
            ByteBuf jsonBuf = content.content();
            String jsonStr = jsonBuf.toString(CharsetUtil.UTF_8);
            if (jsonStr.isEmpty()) {
                throw new IllegalStateException("Empty json");
            }
            command = jsonStr;
        }

        log.info("Channel for main controller server read command {}", command);
        controllerPublisher.handleRequest(command);

        ByteBuf content = Unpooled.copiedBuffer("Command Submitted!", CharsetUtil.UTF_8);
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, content);
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html");
        response.headers().set(HttpHeaderNames.CONTENT_LENGTH, content.readableBytes());
        ctx.write(response);
        ctx.flush();
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
