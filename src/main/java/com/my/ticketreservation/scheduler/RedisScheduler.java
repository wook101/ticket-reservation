package com.my.ticketreservation.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Log4j2
@RequiredArgsConstructor
@Component
public class RedisScheduler {

    private static int cnt = 0;

    private final RedisTemplate<String,String> redisTemplate;

    @Scheduled(fixedRate = 1000) //1초마다 실행
    public void activateUsers() {
        // waiting_queue -> active_users
        // 대기열에서 N명 추출하여 active_users에 삽입
        int insertCnt = 5;
        Set<TypedTuple<String>> data  = redisTemplate.opsForZSet().popMin("waiting_queue",insertCnt);
        log.info("스케줄러 실행 {}: {}", ++cnt, data);

        String[] array = data.stream()
                .map(TypedTuple::getValue)
                .filter(Objects::nonNull)
                .toArray(String[]::new);
        if (array.length > 0) {
            redisTemplate.opsForSet().add("active_users", array);
            redisTemplate.expire("active_users",60, TimeUnit.SECONDS);
            log.info("waiting_queue에서 {}개씩 뽑아서 active_users로 삽입 완료: {}",insertCnt, data);
        }

    }

}
