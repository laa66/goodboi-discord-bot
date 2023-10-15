package com.laa66.goodboi.voiceactivity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;
import reactor.core.publisher.Mono;

@Data
@AllArgsConstructor
@NoArgsConstructor
@With
public class VoiceActivity {

    private String username;
    private Long joinedAt;
    private Long activeTime;

}
