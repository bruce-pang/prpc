package com.brucepang.prpc.common.resource;

import com.brucepang.prpc.logger.Logger;
import com.brucepang.prpc.logger.LoggerFactory;
import com.brucepang.prpc.util.NamedThreadFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Global resource repository between all framework models.
 * It will be destroyed only after all framework model is destroyed.
 */
public class GlobalResourcesRepository {
    private static final Logger logger = LoggerFactory.getLogger(GlobalResourcesRepository.class);

    private volatile static GlobalResourcesRepository instance;
    private volatile ExecutorService executorService;
    private GlobalResourcesRepository() {
    }

    public static GlobalResourcesRepository getInstance() {
        if (instance == null) {
            synchronized (GlobalResourcesRepository.class) {
                if (instance == null) {
                    instance = new GlobalResourcesRepository();
                }
            }
        }
        return instance;
    }

    public ExecutorService getExecutorService() {
        if (executorService == null || executorService.isShutdown()) {
            synchronized (this) {
                if (executorService == null || executorService.isShutdown()) {
                    if (logger.isInfoEnabled()) {
                        logger.info("Creating global shared handler ...");
                    }
                    executorService = Executors.newCachedThreadPool(new NamedThreadFactory("Dubbo-global-shared-handler", true));
                }
            }
        }
        return executorService;
    }

}
