package com.i3e3.mindlet.global.util;

import com.i3e3.mindlet.domain.dandelion.repository.DandelionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
@EnableScheduling
public class SchedulerUtil {

    private final DandelionRepository dandelionRepository;

    @Transactional
    @Scheduled(cron = "0 0/10 * * * *")
    public void checkHoldDandelion() {
        log.info("Hold Dandelion To Flying, TIME={}", LocalDateTime.now());
        long elapsedMinute = 30L;
        dandelionRepository.updateHoldingDandelionToFlying(elapsedMinute);
    }

    @Transactional
    @Scheduled(cron = "0 25 23 * * *")
    public void readyDandelion() {
        log.info("Flying And Hold Dandelion To Ready, TIME={}", LocalDateTime.now());
        dandelionRepository.updateFlyingOrHoldingDandelionToReady();
    }

    @Transactional
    @Scheduled(cron = "0 59 23 * * *")
    public void changeReadyToReturn() {
        log.info("Ready Dandelion To Return, TIME={}", LocalDateTime.now());
        dandelionRepository.updateReadyDandelionToReturn();
    }
}
