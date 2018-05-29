package com.leisen.util;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

import java.net.URL;

//配置文件工具类
public class ConfigUtil {
    public static Configuration config;

    public static void configure(String properties) {
         URL url = ConfigUtil.class.getClassLoader().getResource(properties);
        try {
            config = new PropertiesConfiguration(url);
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }
    }

//    public static void main(String[] args) {
//        ConfigUtil.configure("config.properties");
//    }
}
