package com.brucepang.prpc.singleton;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

/**
 * 根据传入的字节码与参数，生成单例对象
 * @author BrucePang
 */
public enum GenericSingletonFactory {
    INSTANCE;

    private final Map<Class<?>, Object> instances = new HashMap<>();

    public synchronized <T> T getInstance(Class<T> clazz, Object... args) {
        if (!instances.containsKey(clazz)) {
            try {
                // 创建对象
                T instance = createInstance(clazz, args);
                instances.put(clazz, instance);
            } catch (Exception e) {
                throw new RuntimeException("Failed to create instance of " + clazz, e);
            }
        }
        return clazz.cast(instances.get(clazz));
    }

    private <T> T createInstance(Class<T> clazz, Object... args) throws Exception {
        // 获取构造函数
        Class<?>[] parameterTypes = new Class<?>[args.length];
        for (int i = 0; i < args.length; i++) {
            parameterTypes[i] = args[i].getClass();
        }
        Constructor<?> constructor = clazz.getDeclaredConstructor(parameterTypes);

        // 创建对象
        return clazz.cast(constructor.newInstance(args));
    }
}
