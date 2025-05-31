package org.example.predictechmq.socket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class WebSocketBridge {
    private static SimpMessagingTemplate template;

    @Autowired
    public WebSocketBridge(SimpMessagingTemplate template) {
        WebSocketBridge.template = template;
    }

    public static void send(String message) {
        if (template != null) {
            System.out.println("Sending message: " + message);
            template.convertAndSend("/topic/mqtt-data", message);
        }
    }
}
