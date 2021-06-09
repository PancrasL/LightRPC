package github.pancras.factory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author pancras
 * @create 2021/6/5 19:15
 */
public class SingletonFactory {
    private static final Map<String, Object> OBJECT_MAP = new ConcurrentHashMap<>();

    private SingletonFactory() {
    }

    public static <T> T getInstance(Class<T> c) {
        if (c == null) {
            throw new IllegalArgumentException();
        }
        String key = c.toString();
        if (OBJECT_MAP.containsKey(key)) {
            return c.cast(OBJECT_MAP.get(key));
        } else {
            // If the value is null, the value of the second parameter will be stored and returned
            return c.cast(OBJECT_MAP.computeIfAbsent(key, k -> {
                try {
                    return c.getDeclaredConstructor().newInstance();
                } catch (Exception e) {
                    throw new RuntimeException(e.getMessage(), e);
                }
            }));
        }
    }
}
