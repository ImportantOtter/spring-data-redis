package com.example;

import com.example.config.redis.RedisConfig;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringApplication {

    public static void main(String[] args) {
        org.springframework.boot.SpringApplication.run(SpringApplication.class, args);

        RedisConfig redisConfig = new RedisConfig();
        redisConfig.listenExpiredEvents();
    }

}
