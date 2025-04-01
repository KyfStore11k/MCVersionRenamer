package com.kyfstore.mcversionrenamer.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

public class AsyncLogger {
    private final Logger logger;
    private final ExecutorService logExecutor;

    public AsyncLogger(String name) {
        this.logger = LoggerFactory.getLogger(name);
        this.logExecutor = Executors.newSingleThreadExecutor(new CustomThreadFactory(name));
    }

    public void info(String message) {
        logExecutor.submit(() -> logger.info(message));
    }

    public void warn(String message) {
        logExecutor.submit(() -> logger.warn(message));
    }

    public void error(String message) {
        logExecutor.submit(() -> logger.error(message));
    }

    public void error(String message, Throwable throwable) {
        logExecutor.submit(() -> logger.error(message, throwable));
    }

    public void shutdown() {
        logExecutor.shutdown();
        try {
            if (!logExecutor.awaitTermination(3, TimeUnit.SECONDS)) {
                logExecutor.shutdownNow();
            }
        } catch (InterruptedException e) {
            logExecutor.shutdownNow();
        }
    }

    private static class CustomThreadFactory implements ThreadFactory {
        private final String threadName;

        public CustomThreadFactory(String name) {
            this.threadName = name;
        }

        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r);
            thread.setName(threadName);
            return thread;
        }
    }
}