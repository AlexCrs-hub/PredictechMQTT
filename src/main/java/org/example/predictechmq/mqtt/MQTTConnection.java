package org.example.predictechmq.mqtt;

import com.hivemq.client.mqtt.MqttClient;
import com.hivemq.client.mqtt.mqtt5.Mqtt5BlockingClient;
import org.example.predictechmq.socket.WebSocketBridge;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;


import static com.hivemq.client.mqtt.MqttGlobalPublishFilter.ALL;
import static java.nio.charset.StandardCharsets.UTF_8;

public class MQTTConnection {

    private String host;
    private int port;
    private String topic;
    private String username;
    private String password;
    private Mqtt5BlockingClient client;

    public MQTTConnection(String host, int port, String topic, String username, String password) {
        this.host = host;
        this.port = port;
        this.topic = topic;
        this.username = username;
        this.password = password;
    }

    public void connect() {
        this.client = MqttClient.builder()
                .useMqttVersion5()
                .serverHost(this.host)
                .serverPort(this.port)
                .sslWithDefaultConfig()
                .buildBlocking();

        client.connectWith()
                .simpleAuth()
                .username(this.username)
                .password(UTF_8.encode(this.password))
                .applySimpleAuth()
                .send();

        System.out.println("Connected successfully");

        client.subscribeWith()
                .topicFilter(this.topic)
                .send();
        System.out.println("Client subscriped to my/test/topic");

        client.toAsync().publishes(ALL, publish -> {
            String message = String.valueOf(UTF_8.decode(publish.getPayload().get()));
            System.out.println(message);
            WebSocketBridge.send(message);
        });
    }

    public void getMessage(){

    }

    public void disconnect() {
        this.client.disconnect();
    }

}
