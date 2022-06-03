package com.ScalableTeam.services.managers;

import com.ScalableTeam.utils.StringUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

@Component
@Slf4j
@AllArgsConstructor
public class ClassManager {
    private final ConfigurableListableBeanFactory beanFactory;
    private final ConfigurableApplicationContext ctx;
    private final ByteClassLoader byteClassLoader;

    public void addCommand(String className, byte[] b) {
        Class<?> clazz = byteClassLoader.loadClassFromBytes(className, b);
        addCommand(clazz, className);
    }

    private void addCommand(final Class<?> clazz, String className) {
        try {
            Constructor<?> cons = clazz.getConstructor();
            Object instance = cons.newInstance();
            String simpleName = StringUtils.camelCase(StringUtils.getClassSimpleName(className));
            injectCommand(instance, simpleName);
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException | IllegalStateException e) {
            e.printStackTrace();
            log.error("Could not add class with name {}", className);
        }
    }

    private void injectCommand(Object command, String commandName) {
        this.beanFactory.initializeBean(command, commandName);
        this.beanFactory.autowireBean(command);
        this.beanFactory.registerSingleton(commandName, command);
        log.info("Beans of type {}: {}", command.getClass().getSimpleName(), ctx.getBeansOfType(command.getClass()));
    }

    public void deleteCommand(String commandName) {
        String simpleName = StringUtils.camelCase(StringUtils.getClassSimpleName(commandName));
        ((DefaultListableBeanFactory) beanFactory).destroySingleton(simpleName);
    }
}

