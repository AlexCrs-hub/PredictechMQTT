package org.example.predictechmq.redis;

import org.example.predictechmq.socket.WebSocketBridge;
import org.springframework.stereotype.Service;

@Service
public class RedisMessageSubscriber {
    public void onMessage(String message, String channel) {
        System.out.println("Received from Redis channel: " + message);
        WebSocketBridge.send(message);  // Push to connected WebSocket clients
    }
}
