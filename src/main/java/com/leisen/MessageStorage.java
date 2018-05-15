package com.leisen;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 消息仓库
 */
public class MessageStorage {
    private static final int CAPACITY_DEFAULT = 20000;
    private static final ExecutorService EXECUTOR_SERVICE_PRODUCER = Executors.newFixedThreadPool(2);
    private static final ExecutorService EXECUTOR_SERVICE_CONSUMER = Executors.newFixedThreadPool(3);
    protected static BlockingQueue<Object> queue = new LinkedBlockingQueue<Object>(CAPACITY_DEFAULT);

//    public MessageStorage() {
//        queue = new LinkedBlockingQueue<Object>(CAPACITY_DEFAULT);
//    }

    /**
     * 生产者任务调度方法
     * @param producer
     * @param <T>
     */
    public static <T extends Runnable> void executeProducer(T producer) {
        EXECUTOR_SERVICE_PRODUCER.submit(producer);
    }

    /**
     * 消费者任务调度方法
     * @param consumer
     * @param <T>
     */
    public static <T extends Runnable> void executeConsumer(T consumer) {
        EXECUTOR_SERVICE_CONSUMER.submit(consumer);
    }

    public MessageStorage(int capacity) {
        queue = new LinkedBlockingQueue<Object>(capacity);
    }

}
