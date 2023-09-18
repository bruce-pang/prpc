package com.brucepang.prpc.handler.reflect;

import com.brucepang.prpc.core.RpcRequest;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author BrucePang
 * 委托类：定义好接口与method的映射关系
 */
public class Mediator {
    public static Map<String, BeanMethod> beanMethodMap = new ConcurrentHashMap<>();
    private volatile static Mediator instance = null;


    public static Mediator getInstance() {
        if (instance == null) {
            synchronized (Mediator.class) {
                if (instance == null) {
                    instance = new Mediator();
                }
            }
        }
        return instance;
    }

    /**
     * 从request请求中获取到类名和方法名，然后从beanMethodMap中获取到对应的bean和method，然后执行反射调用method
     * @param request
     * @return
     */
    public Object processor(RpcRequest request){
        String key = request.getClassName() + "." + request.getMethodName();
        BeanMethod beanMethod = beanMethodMap.get(key);
        if (beanMethod == null){
            return null;
        }
        Object bean = beanMethod.getBean();
        Method method = beanMethod.getMethod();
        try {
            return method.invoke(bean, request.getParams());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
}
