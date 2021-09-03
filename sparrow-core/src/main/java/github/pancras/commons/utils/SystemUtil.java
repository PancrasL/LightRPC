package github.pancras.commons.utils;

/**
 * @author PancrasL
 */
public class SystemUtil {
    private SystemUtil() {
    }

    /**
     * 获取处理器的个数
     *
     * @return 处理器个数
     */
    public static int getAvailableProcessorNum() {
        return Runtime.getRuntime().availableProcessors();
    }
}
