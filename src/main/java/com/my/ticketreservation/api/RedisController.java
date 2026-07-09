package com.my.ticketreservation.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;


@Log4j2
@RestController
@RequiredArgsConstructor
public class RedisController {

    private final RedisTemplate<String,String> redisTemplate;

    @PostMapping("/queue")
    public String queue() {
        String key = "waiting_queue"; //waiting:reserve:1, waiting:reserve:2 예약명으로 나누어 처리가능
        String userId = "user_" + (int)(Math.random()*100);
        long timestamp = System.currentTimeMillis();
        addAndExpire(key, userId, timestamp, 30);

        return "push in queue";
    }

    public void addAndExpire(String key, String userId, long timestamp, int seconds) {
        log.info("key: {}, userId: {}, score: {}", key, userId, timestamp);
        redisTemplate.opsForZSet().add(
                key,
                userId,
                timestamp
        );
        //redisTemplate.expire(key, seconds, TimeUnit.SECONDS);
    }

    @GetMapping("/queue/status")
    public void queueStatus() {
    }

    @GetMapping("/active/check")
    public void activeCheck(){
    }

}
