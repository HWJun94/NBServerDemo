package com.leisen.msgqueue;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leisen.util.ConfigUtil;
import com.leisen.mqtt.MqttPublishServer;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;

/**
 * 队列消息消费者（消费队列中的消息）
 */
public class QueueMessageConsumer implements QueueMessagePorter {

    private static final Logger logger = LoggerFactory.getLogger(QueueMessageConsumer.class);

    private MqttPublishServer mqttPublishServer;

    //constructor
    public QueueMessageConsumer() {
        try {
            this.mqttPublishServer = new MqttPublishServer();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public QueueMessageConsumer(MqttPublishServer mqttPublishServer) {
        this.mqttPublishServer = mqttPublishServer;
    }

    @Override
    public void run() {
        while(true) {
            try {
                Object message = MessageStorage.queue.take();
                ObjectMapper mapper = new ObjectMapper();
                Map map = mapper.readValue((String)message, Map.class);
                String deviceId = (String) map.get("deviceId");
                String IMEI = ConfigUtil.config.getString(deviceId);
                String strTopic = "bupt/admin/" + IMEI;
                Object service = map.get("service");
                String dataStr = mapper.writeValueAsString(service);
                mqttPublishServer.publish(strTopic, dataStr);
                logger.info("ConsumedMessage:\n{}", message);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (MqttException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
