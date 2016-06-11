package com.clara.deploy.domain;


import java.io.IOException;
import java.util.Properties;

/**
 * Created by IntelliJ IDEA.
 * User: cailong
 * Date: 14-1-22
 * Time: 下午1:47
 * To change this template use File | Settings | File Templates.
 */
public class PropertiesHolder {
    public static final String PROPERTIES_PATH = "/baseInfo.properties";
    private static Properties prop = null;

    static {
        try {
            prop = PropertiesLoader.load(PROPERTIES_PATH);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String get(String key) {
        return prop.getProperty(key);
    }
}
