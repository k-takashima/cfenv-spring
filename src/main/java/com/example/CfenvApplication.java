package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class CfenvApplication {

	public static void main(String[] args) {
		SpringApplication.run(CfenvApplication.class, args);
	}

    @Bean
    StringRedisTemplate stringRedisTemplate(RedisTemplate template) {
        return new StringRedisTemplate(template.getConnectionFactory());
    }

    @Bean
    RedisAtomicLong redisAtomicLong(StringRedisTemplate template) {
        return new RedisAtomicLong("visits", template.getConnectionFactory());
    }

}

@RestController
class CfenvController {
    @Autowired
    private RedisAtomicLong visits;

    @RequestMapping("/")
    public CloudFoundryDemo cloudFoundryDemo(@Value("${CF_INSTANCE_INDEX:0}") int instance) {
        return new CloudFoundryDemo (instance, visits.incrementAndGet());
    }
}


class CloudFoundryDemo {
    private int instance;
    private long visits;

    public CloudFoundryDemo(int instance, long visits) {
        this.instance = instance;
        this.visits = visits;
    }

    public int getInstance() {
        return instance;
    }

    public long getVisits() {
        return visits;
    }
}
