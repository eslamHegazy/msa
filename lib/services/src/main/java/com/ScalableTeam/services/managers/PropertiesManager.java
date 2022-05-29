package com.ScalableTeam.services.managers;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;

@Component
public class PropertiesManager {
    private final Properties configProperties = new Properties();

    public void loadProperties() throws IOException {
        loadConfigProperties();
    }

    public void loadConfigProperties() throws IOException {
        final InputStream inputStream = getClass().getClassLoader().getResourceAsStream("application.properties");
        configProperties.load(inputStream);
    }

    private Integer readIntProperty(String propertyName) {
        if (!configProperties.containsKey(propertyName))
            return null;
        return Integer.parseInt(configProperties.getProperty(propertyName));
    }

    private ArrayList<String> readArrayProperty(String propertyName, String delimiter) {
        if (!configProperties.containsKey(propertyName))
            return null;
        return new ArrayList<>(Arrays.asList(configProperties.getProperty(propertyName).split(delimiter)));
    }

    public void updateProperty(String propertyName, String newValue) {
        configProperties.replace(propertyName, newValue);
    }

//    public Map<String, DBConfig> getRequiredDbs() {
//        final Map<String, DBConfig> requiredDbs = new HashMap<>();
//        ArrayList<String> requiredDBsList = readArrayProperty(ServiceConstants.REQUIRED_DATABASES_PROPERTY_NAME,
//                ServiceConstants.REQUIRED_DATABASES_ARRAY_DELIMITER,
//                ServiceConstants.DEFAULT_REQUIRED_DATABASES);
//
//        for (String dbPair : requiredDBsList) {
//            String[] split = dbPair.split(ServiceConstants.REQUIRED_DATABASES_PAIR_DELIMITER);
//            String classPath = split[0];
//            int connectionCount = Integer.parseInt(split[1]);
//            requiredDbs.put(classPath, new DBConfig(connectionCount));
//        }
//
//        System.out.println("Required Dbs: " + requiredDbs);
//        return requiredDbs;
//    }

//    public int getControllerPort() {
//        final InputStream stream = getClass().getClassLoader().getResourceAsStream("apps-ports.properties");
//        final Properties properties = new Properties();
//        try {
//            properties.load(stream);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        String propertyName = appUriName.toLowerCase();
//        return Integer.parseInt(properties.getProperty(propertyName));
//
//    }
//
//    public int getThreadCount() {
//        return readIntProperty(ServiceConstants.THREADS_COUNT_PROPERTY_NAME, ServiceConstants.DEFAULT_THREADS_COUNT);
//    }
}
    