package github.pancras.commons.utils;

/**
 * @author pancras
 * @create 2021/6/24 9:36
 */
public class SystemUtil {
    private SystemUtil() {
    }

    public static int getAvailableProcessors() {
        return Runtime.getRuntime().availableProcessors();
    }
}
