package com.leisen.mqtt;

import com.leisen.db.DeviceStorage;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MqttPushCallback implements MqttCallback{
    public static final Logger logger = LoggerFactory.getLogger(MqttPushCallback.class);

    MqttPublishServer mqttServer;

    public void setMQTTServer(MqttPublishServer pServer  )
    {
        mqttServer = pServer;
    }

    public void connectionLost(Throwable cause) {
        // 连接丢失后，一般在这里面进行重连
        cause.printStackTrace();
//        System.out.println("连接断开，正在重连");
        logger.warn("连接断开，正在重连");
        mqttServer.connect();
    }

    public void deliveryComplete(IMqttDeliveryToken token) {
        //publish之后会执行到这里
        System.out.println("deliveryComplete--" + token.isComplete());

    }

    public void messageArrived(String topic, MqttMessage message) {
        // subscribe后得到的消息会执行到这里面
        String[] topicSplit = topic.split("/");
        String IMEI = topicSplit[topicSplit.length - 1];
        DeviceStorage deviceStorage = new DeviceStorage();
        String deviceId = deviceStorage.getDeviceMap().get(IMEI);
        if (deviceId == null)
            return;
        String messageStr = new String(message.getPayload());

        //通过httpclient下发命令请求
        //TODO

        logger.info("接收消息主题 : {}", topic);
        logger.info("接收消息Qos : {}", message.getQos());
        logger.info("接收消息内容 : {}", new String(message.getPayload()));
    }
}

