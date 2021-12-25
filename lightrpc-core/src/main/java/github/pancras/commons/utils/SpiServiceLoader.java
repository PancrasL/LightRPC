package github.pancras.commons.utils;

import java.util.ServiceLoader;

/**
 * 基于java.util.ServiceLoader实现的SPI类加载器
 */
public class SpiServiceLoader {
    /**
     * 加载找到的第一个接口实现类
     *
     * @param interfac 接口类
     * @param <T>      接口类型
     * @return 接口T的实现类
     * @throws IllegalStateException 当未找到接口实现时抛出
     */
    public static <T> T loadService(Class<T> interfac) {
        ServiceLoader<T> loader = ServiceLoader.load(interfac);
        for (T service : loader) {
            return service;
        }

        throw new IllegalStateException("Can not find impl of interface: " + interfac.getCanonicalName());
    }


    /**
     * @param interfac  接口类信息
     * @param className 类的全限定名，形如java.lang.String
     * @param <T>       接口类型
     * @return 类名伪className的接口实现
     * @throws IllegalStateException 当未找到接口实现时抛出
     */
    public static <T> T loadServiceByName(Class<T> interfac, String className) {
        ServiceLoader<T> loader = ServiceLoader.load(interfac);
        for (T service : loader) {
            if (service.getClass().getCanonicalName().equals(className)) {
                return service;
            }
        }

        throw new IllegalStateException(String.format("Can not find %s impl of interface: %s", className, interfac.getCanonicalName()));
    }
}
