package com.laa66.goodboi.voiceactivity;

import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;

import java.time.*;

@AllArgsConstructor
public class VoiceChannelActivityTask {

    private final VoiceChannelActivityRepository voiceChannelActivityRepository;

    public Flux<Long> scheduleRepositoryTask(int hour, int min) {
        long currentTime = LocalDateTime.now()
                .atZone(ZoneId.systemDefault())
                .toEpochSecond();
        long startTime = LocalDateTime.of(LocalDate.now(), LocalTime.of(hour, min, 59))
                .atZone(ZoneId.systemDefault())
                .toEpochSecond();
        return Flux.interval(Duration.ofSeconds(startTime - currentTime), Duration.ofDays(1))
                .doOnEach(longSignal -> voiceChannelActivityRepository.clear());
    }


}
