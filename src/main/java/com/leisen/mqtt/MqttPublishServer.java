package com.leisen.mqtt;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

/**
 * Title:Server 这是发送消息的服务端 Description: 服务器向多个客户端推送主题，即不同客户端可向服务器订阅相同主题
 */
public class MqttPublishServer {
    public static final Logger logger = LoggerFactory.getLogger(MqttPublishServer.class);

    // tcp://MQTT安装的服务器地址:MQTT定义的端口号
    public static final String HOST = "tcp://mqttnewtest.mqtt.iot.gz.baidubce.com:1883";
    // 定义一个主题
    public static final String TOPIC = "newtopic";
    // 定义MQTT的ID，可以在MQTT服务配置中指定
    private static final String clientid = "mqttnewuser";

    private MqttClient client;
    MqttConnectOptions options;

    private MqttTopic topic;
//	private MqttMessage message;

    private String userName = "mqttnewtest/mqttnewuser"; // 非必须
    private String passWord = "9ovm+mk2rdfqAfI0oxHBEa5TVrzfMa2mcgX8UBeAvj0="; // 非必须


    /**
     * 构造函数
     */
    public MqttPublishServer() throws MqttException {
        // MemoryPersistence设置clientid的保存形式，默认为以内存保存

//		connect();
    }

    public int initMQTT()
    {
        try {
            client = new MqttClient(HOST, clientid, new MemoryPersistence());
        }catch(Exception e) {
            e.printStackTrace();
            return 1;
        }
        options = new MqttConnectOptions();
        options.setCleanSession(false);
        options.setUserName(userName);
        options.setPassword(passWord.toCharArray());
        // 设置超时时间
        options.setConnectionTimeout(10);
        // 设置会话心跳时间
        options.setKeepAliveInterval(20);

        MqttPushCallback pck = new MqttPushCallback();
        pck.setMQTTServer(this);
        client.setCallback(pck);

        return 0;
    }

    /**
     * 连接服务器
     */
    public void connect() {

        try {

            client.connect(options);
            // 设置topic主题，进行订阅,可以实现多主题订阅
            int[] Qos = { 1 };
            String[] topic1 = { "newtopic"};
            client.subscribe(topic1, Qos);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置发布消息
     */
    public void publish( String strTopic ,String strMessage) throws MqttPersistenceException, MqttException {
        // 初始化后设置message，获得topic，并连接服务器
        MqttMessage mqttMessage = new MqttMessage();
        mqttMessage.setQos(1); // 保证消息能到达一次
        mqttMessage.setRetained( false );
        mqttMessage.setPayload( strMessage.getBytes() );

//        //System.out.println(server.topic);
//        System.out.println(strTopic);
//        System.out.println(mqttMessage);

        topic = client.getTopic(strTopic);
        MqttDeliveryToken token = topic.publish(mqttMessage);

        token.waitForCompletion();

    }

//    /**
//     * 启动
//     */
//    public static void main(String[] args) throws MqttException {
//
//        // 创建publishServer之后首先应该进行客户端初始化
//        MqttPublishServer server = new MqttPublishServer();
//        server.initMQTT();
//        // 连接服务器
//        server.connect();
//        String mString ;
//
//        Scanner in;
//        System.out.println("输入要发布的消息");
//
//        do {
//            in = new Scanner(System.in);
//            mString = in.next();
//
//            // topic massage获得后进行发布
//            server.publish(TOPIC,mString );
//        }while( !mString.equalsIgnoreCase("exit"));
//
//    }
}
