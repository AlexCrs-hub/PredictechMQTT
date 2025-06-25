package org.example.predictechmq.mqtt;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hivemq.client.mqtt.MqttClient;
import com.hivemq.client.mqtt.mqtt5.Mqtt5BlockingClient;
import org.example.predictechmq.api.ApiCommunication;
import org.example.predictechmq.model.Reading;
import org.example.predictechmq.redis.RedisMessagePublisher;
import org.example.predictechmq.socket.WebSocketBridge;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;


import java.io.IOException;

import static com.hivemq.client.mqtt.MqttGlobalPublishFilter.ALL;
import static java.nio.charset.StandardCharsets.UTF_8;

@Component
public class MQTTConnection {

    @Autowired
    private RedisMessagePublisher redisPublisher;

    private String host;
    private int port;
    private String topic;
    private String username;
    private String password;
    private Mqtt5BlockingClient client;

    public MQTTConnection( @Value("${mqtt.host}") String host,
                           @Value("${mqtt.port}") int port,
                           @Value("${mqtt.topic}") String topic,
                           @Value("${mqtt.username}") String username,
                           @Value("${mqtt.password}") String password) {
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
            ObjectMapper mapper = new ObjectMapper();
            try {
                JsonNode root = mapper.readTree(message);
                ApiCommunication api = new ApiCommunication();
                root.fields().forEachRemaining(entry -> {
                    String sensor = entry.getKey();
                    double measurement = entry.getValue().asDouble();
                    Reading reading = new Reading(sensor, measurement); // Adjust constructor as needed
                    try {
                        api.addMeasurements(reading);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
            redisPublisher.publish("mqtt-data", message);
        });
    }

    public void getMessage(){

    }

    public void disconnect() {
        this.client.disconnect();
    }

}
