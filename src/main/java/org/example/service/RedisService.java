package org.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

@Service
public class RedisService {

    @Value("${redis.events.ttl}")
    private long eventsLifeTime;
    private RedisTemplate<String, Object> redisTemplate;
    private HashOperations<String, String, Object> hashOps;

    @Autowired
    private RedisService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @PostConstruct
    private void init() {
        hashOps = redisTemplate.opsForHash();
    }

    public void saveUserInfo(String id, Object object,long eventsLifeTime) {
        hashOps.put(id, id, object);
        redisTemplate.expire(id, eventsLifeTime, TimeUnit.MINUTES);
    }

    public Object findUserInfo(String id) {
        return (Object) hashOps.get(id, id);
    }

    public void deleteUserInfo(String id) {
        hashOps.delete(id, id);
    }

    public void updateUserInfo(String id, Object object,long eventsLifeTime) {
        hashOps.put(id, id, object);
        redisTemplate.expire(id, eventsLifeTime, TimeUnit.MINUTES);
    }
}
