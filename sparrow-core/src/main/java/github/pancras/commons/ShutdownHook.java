package github.pancras.commons;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author PancrasL
 */
public class ShutdownHook extends Thread {
    private static final Logger LOGGER = LoggerFactory.getLogger(ShutdownHook.class);

    private static final ShutdownHook SHUTDOWN_HOOK = new ShutdownHook("ShutdownHook");

    static {
        Runtime.getRuntime().addShutdownHook(SHUTDOWN_HOOK);
    }

    private final ArrayList<Disposable> disposables = new ArrayList<>();
    private final AtomicBoolean destroyed = new AtomicBoolean(false);

    private ShutdownHook(String name) {
        super(name);
    }

    public static ShutdownHook getInstance() {
        return SHUTDOWN_HOOK;
    }

    @Override
    public void run() {
        destroyAll();
    }

    public void addDisposable(Disposable disposable) {
        disposables.add(disposable);
    }

    public void destroyAll() {
        if (!destroyed.compareAndSet(false, true)) {
            return;
        }
        if (disposables.isEmpty()) {
            return;
        }
        LOGGER.debug("destroyAll starting.");
        for (Disposable disposable : disposables) {
            disposable.destroy();
        }
        LOGGER.debug("destroyAll finish.");
    }
}
