package com.clara.deploy.domain;

import sun.misc.Launcher;
import sun.misc.URLClassPath;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by IntelliJ IDEA.
 * User: cailong
 * Date: 14-1-22
 * Time: 下午1:48
 * To change this template use File | Settings | File Templates.
 */
public class PropertiesLoader {
    public static Properties load(String path) throws IOException {
        Properties prop = new Properties();
        InputStream is = null;

        URLClassPath ucp = Launcher.getBootstrapClassPath();
        try {
            is = PropertiesLoader.class.getResourceAsStream(path);
            prop.load(is);

            if (null != is) {
                is.close();
            }
        } catch (IOException e) {
            if (null != is) {
                is.close();
            }
            throw e;
        }
        return prop;
    }
}

