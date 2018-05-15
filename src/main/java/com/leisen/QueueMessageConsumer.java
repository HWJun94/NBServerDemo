package com.leisen;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 队列消息消费者（消费队列中的消息）
 */
public class QueueMessageConsumer implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(QueueMessageConsumer.class);

    @Override
    public void run() {
        try {
            Object message = MessageStorage.queue.take();
            logger.info("ConsumedMessage:\n{}", message);
//            System.out.println(Thread.currentThread().getName() + "消费者: " + message);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
