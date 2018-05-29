package com.leisen.msgqueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 队列消息生产者（生产队列消息）
 */
public class QueueMessageProducer implements QueueMessagePorter {
    private static final Logger logger = LoggerFactory.getLogger(QueueMessageConsumer.class);

    private Object message;

    public QueueMessageProducer(Object message) {
        this.message = message;
    }

    @Override
    public void run() {
        if (message == null)
            throw new RuntimeException("The message of QueueMessageProducer can't be null value!");
        try {
            MessageStorage.queue.put(message);
            logger.info("ProductedMessage:\n{}", message);
//            System.out.println(Thread.currentThread().getName() + "生产者: " + message);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
