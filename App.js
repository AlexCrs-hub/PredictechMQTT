import logo from './logo.svg';
import './App.css';
import React, { useEffect, useState } from 'react';
import SockJS from 'sockjs-client';
import { Client } from '@stomp/stompjs';

function App() {
  const [messages, setMessages] = useState([]);
  useEffect(() => {
    const socket = new SockJS('http://localhost:8080/ws');
    const client = new Client({
      webSocketFactory: () => socket,
      debug: (str) => { console.log(str); },
      onConnect: () => {
        console.log('Connected');
        client.subscribe('/topic/mqtt-data', (message) => {
          console.log('Received message:', message.body);
          setMessages((prevMessages) => [...prevMessages, message.body]);
        });
      },
    });
    client.activate();
    return () => {
      client.deactivate();
    };
  }, []);
  return (
    <div className="App">
      <h2>MQTT Data from WebSocket</h2>
      <ul>
        {messages.map((msg, idx) => (
          <li key={idx}>{msg}</li>
        ))}
      </ul>
    </div>
  );
}

export default App;
