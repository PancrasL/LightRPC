package github.pancras.commons.utils;

import java.util.ServiceLoader;

/**
 * 基于java.util.ServiceLoader实现的SPI类加载器
 */
public class SpiServiceLoader {
    public static <T> T loadService(Class<T> clazz) {
        ServiceLoader<T> loader = ServiceLoader.load(clazz);
        for (T service : loader) {
            return service;
        }
        throw new IllegalStateException("Can not find impl of interface: " + clazz.getCanonicalName());
    }
}
