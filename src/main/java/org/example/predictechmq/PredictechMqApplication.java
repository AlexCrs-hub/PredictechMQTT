package org.example.predictechmq;

import jakarta.annotation.PostConstruct;
import org.example.predictechmq.mqtt.MQTTConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PredictechMqApplication {

    @Autowired
    private MQTTConnection connection;

    public static void main(String[] args) {
        SpringApplication.run(PredictechMqApplication.class, args);
    }

    @PostConstruct
    public void startMqtt() {
        connection.connect();
    }

}
