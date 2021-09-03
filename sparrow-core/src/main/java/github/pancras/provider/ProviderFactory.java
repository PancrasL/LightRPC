package github.pancras.provider;

import github.pancras.provider.impl.DefaultProviderServiceImpl;

/**
 * @author PancrasL
 */
public class ProviderFactory {
    private static volatile ProviderService instance = null;

    private ProviderFactory() {
    }

    public static ProviderService getInstance() {
        if (instance == null) {
            synchronized (ProviderFactory.class) {
                if (instance == null) {
                    instance = new DefaultProviderServiceImpl();
                }
            }
        }
        return instance;
    }
}
