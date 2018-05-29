package com.leisen.msgqueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 消息仓库
 */
public class MessageStorage {
    private static final Logger logger = LoggerFactory.getLogger(MessageStorage.class);

    private static final int CAPACITY_DEFAULT = 20000;
    private static final int NTHREADS_CONSUMER = 3;
    private static final ExecutorService EXECUTOR_SERVICE_PRODUCER = Executors.newFixedThreadPool(2);
    private static final ExecutorService EXECUTOR_SERVICE_CONSUMER = Executors.newFixedThreadPool(NTHREADS_CONSUMER);
    protected static BlockingQueue<Object> queue = new LinkedBlockingQueue<Object>(CAPACITY_DEFAULT);

    //constructor
    public MessageStorage() {
//        queue = new LinkedBlockingQueue<Object>(CAPACITY_DEFAULT);
    }

    public MessageStorage(int capacity) {
        queue = new LinkedBlockingQueue<Object>(capacity);
    }

    //method
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

    /**
     * 消息入库方法
     * @param message
     */
    public static void put(Object message) {
        try {
            MessageStorage.queue.put(message);
            logger.info("ProductedMessage:\n{}", message);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 消息出库方法
     * @return
     * @throws InterruptedException
     */
    public static Object take() throws InterruptedException {
        return MessageStorage.queue.take();
    }

    public int getNthreadsConsumer() {
        return NTHREADS_CONSUMER;
    }
}
