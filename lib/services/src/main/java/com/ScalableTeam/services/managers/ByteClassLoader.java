package com.ScalableTeam.services.managers;

import com.ScalableTeam.utils.StringUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

@Component
public class ByteClassLoader extends ClassLoader {

    public Class<?> loadClassFromBytes(String name, byte[] b) {
        try {
            return this.defineClass(name, b, 0, b.length);
        } catch (LinkageError e) {
            return new ByteClassLoader().loadClassFromBytes(name, b);
        }
    }

    public static byte[] readClassFileAsBytes(String className) throws IOException {
        byte[] buffer;
        final String path = StringUtils.getClassSimpleName(className) + StringUtils.CLASS_EXT;
        System.out.println("readClassFilesAsBytes: path: " + path);
        try (final InputStream inputStream = new ClassPathResource(path).getInputStream()) {
            buffer = inputStream.readAllBytes();
        }

        return buffer;
    }
}