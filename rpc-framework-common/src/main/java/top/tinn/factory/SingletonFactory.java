package top.tinn.factory;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Tinn
 * @Date: 2020/8/11 15:14
 */
public final class SingletonFactory {
    //使用Map作为内存缓存
    private static final Map<String, Object> OBJECT_MAP = new HashMap<>();
    private SingletonFactory(){}
    public static <T> T getInstance(Class<T> clazz){
        String key  = clazz.toString();
        Object instance = OBJECT_MAP.get(key);
        if (instance == null){
            synchronized (clazz){
                if (instance == null){
                    try {
                        instance = clazz.newInstance();
                        OBJECT_MAP.put(key, instance);
                    } catch (InstantiationException | IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return clazz.cast(instance);
    }
}
