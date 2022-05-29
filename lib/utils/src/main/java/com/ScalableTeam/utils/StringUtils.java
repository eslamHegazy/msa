package com.ScalableTeam.utils;

import io.netty.buffer.ByteBuf;

public class StringUtils {
    public static String byteBufferToString(Object msg) {
        ByteBuf buf = (ByteBuf) msg;
        StringBuilder sb = new StringBuilder();
        while (buf.isReadable()) {
            sb.append((char) buf.readByte());
        }
        return sb.toString();
    }
}
