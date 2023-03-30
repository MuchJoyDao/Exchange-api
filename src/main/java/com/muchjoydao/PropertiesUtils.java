package com.muchjoydao;

import cn.hutool.core.lang.Console;
import cn.hutool.core.util.StrUtil;

import java.io.*;
import java.util.Objects;
import java.util.Properties;

public class PropertiesUtils {
    /**
     * properties
     */
    private static Properties properties = null;
    private static final String fileName = "coin.properties";

    /**
     * 根据key获取value值
     */
    public static String getValue(String key) {
        if (properties == null) {
            properties = loadConfProperties();
        }
        String value = properties.getProperty(key);
        if (StrUtil.isNotBlank(value) && (key.contains("apiKey") || key.contains("secretKey"))) {
            Console.log("从配置文件读取参数[{}] -> {}", key, value.substring(0, 5) + "************" + value.substring(value.length() - 5));
        } else if (!key.startsWith("common.")) {
            Console.log("从配置文件读取参数[{}] -> {}", key, value);
        }
        return value;
    }

    /**
     * 初始化properties
     */
    public static Properties loadConfProperties() {
        Properties properties = new Properties();
        InputStream in = null;
        // 优先从项目路径获取连接信息
        String confPath = System.getProperty("user.dir");

        confPath = confPath + File.separator + fileName;
        File file = new File(confPath);
        if (file.exists()) {
            Console.log("配置文件路径->{}", confPath);
            try {
                in = new FileInputStream(confPath);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        // 未传入路径时，读取classpath路径
        else {
            in = PropertiesUtils.class.getClassLoader().getResourceAsStream(fileName);
            if (Objects.nonNull(in)) {
                Console.log("从classpath路径下加载配置文件{}", fileName);
            }
        }
        if (Objects.isNull(in)) {
            throw new RuntimeException("未找到配置文件[" + fileName + "]");
        }
        try {
            properties.load(in);
        } catch (IOException ignored) {
        }
        return properties;
    }
}
