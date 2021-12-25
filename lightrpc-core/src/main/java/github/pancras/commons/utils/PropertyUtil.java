package github.pancras.commons.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Properties;

/**
 * @author PancrasL
 */
public class PropertyUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(PropertyUtil.class);
    private static final Properties PROPERTIES = new Properties();

    public static String getProperty(String key, String defaultValue) {
        try {
            PROPERTIES.load(PropertyUtil.class.getResourceAsStream("/lightrpc.properties"));
        } catch (IOException e) {
            LOGGER.warn("Can not find lightrpc.properties");
            return defaultValue;
        }
        return PROPERTIES.getProperty(key, defaultValue);
    }
}
