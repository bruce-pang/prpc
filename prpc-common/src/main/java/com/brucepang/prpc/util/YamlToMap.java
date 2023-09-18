package com.brucepang.prpc.util;

import org.yaml.snakeyaml.Yaml;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 为了方便操作在beandefination期间的yml配置文件，需要转换为Java中的Map类型
 * @author BrucePang
 */
public class YamlToMap {
    /**
     * 转换yaml文件为key,value的形式，方便操作
     * @param filePath 文件位置
     * @return
     */
    public static Map<String, Object> yamlToMap(String filePath) throws FileNotFoundException {
        try{
        // 1. 使用流关联yml配置文件
        BufferedReader br = new BufferedReader(new FileReader(YamlToMap.class.getClassLoader().getResource(filePath).getPath()));

        // 2. 创建yaml工具类
        Yaml yaml = new Yaml();

        // 3. 使用yaml工具类加载 流
        // 返回的字符形式非常类似于JSON, 但又不是JSON, 因为返回的字符之间使用的是 = 号连接的
        // 可以直接转换为Map 进行处理, key 的类型是 字符串, value的类型为Object
        Map<String, Object> yamlMap = (Map<String, Object>) yaml.load(br);

        // 创建自定义格式的 Map
        Map<String, Object> customMap = new LinkedHashMap<>();
        flattenMap("", yamlMap, customMap);

        // 现在你可以使用 customMap 对象来操作 YAML 数据
        System.out.println(customMap);
        return customMap;
    } catch (IOException e) {
        e.printStackTrace();
    }
        return null;
}

    private static void flattenMap(String prefix, Map<String, Object> sourceMap, Map<String, Object> targetMap) {
        for (Map.Entry<String, Object> entry : sourceMap.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            String newKey = prefix.isEmpty() ? key : prefix + "." + key;

            if (value instanceof Map) {
                flattenMap(newKey, (Map<String, Object>) value, targetMap);
            } else {
                targetMap.put(newKey, value);
            }
        }
    }
}