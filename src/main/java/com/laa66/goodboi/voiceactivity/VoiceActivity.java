package com.laa66.goodboi.voiceactivity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

@Data
@AllArgsConstructor
@NoArgsConstructor
@With
public class VoiceActivity {
    private Long joinedAt;
    private Long activeTime;

}
