package com.example.config;

import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisPubSub;

@Component
public class RedisExpirationListener extends JedisPubSub {

    @Override
    public void onPSubscribe(String pattern, int subscribedChannels) {
        System.out.println("Subscribed to expired events on channel: ");
    }

    @Override
    public void onPMessage(String pattern, String channel, String message) {
        System.out.println("Key expired on channel " + message);
    }
}