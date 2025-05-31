package org.example.predictechmq;

import org.example.predictechmq.mqtt.MQTTConnection;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.messaging.simp.SimpMessagingTemplate;

@SpringBootApplication
public class PredictechMqApplication {

    public static void main(String[] args) {
        SpringApplication.run(PredictechMqApplication.class, args);
        String topic = "my/test/topic";
        int port = 8883;
        final String host = "90b9372050704848bdf5aea514a21e25.s1.eu.hivemq.cloud";
        final String username = "Yousef";
        final String password = "Yousef123";
        MQTTConnection connection = new MQTTConnection(host, port, topic ,username, password);
        connection.connect();
    }

}
