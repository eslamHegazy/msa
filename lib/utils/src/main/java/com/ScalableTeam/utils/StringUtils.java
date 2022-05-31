package com.ScalableTeam.utils;

import io.netty.buffer.ByteBuf;

import java.util.Arrays;
import java.util.Locale;

public class StringUtils {
    public static final String CLASS_EXT = ".class";
    public static final char PACKAGE_SEPARATOR = '.';

    public static String byteBufferToString(Object msg) {
        ByteBuf buf = (ByteBuf) msg;
        StringBuilder sb = new StringBuilder();
        while (buf.isReadable()) {
            sb.append((char) buf.readByte());
        }
        return sb.toString();
    }

    public static String getMethodName(String name) {
        return "get" + name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    public static String getClassSimpleName(String packageName) {
        System.out.println(packageName);
        String[] name = packageName.split(".");
        System.out.println(Arrays.toString(name));
        return camelCase(name[name.length - 1]);
    }

    public static String camelCase(String str) {
        return str.substring(0, 1).toLowerCase() + str.substring(1);
    }
}
