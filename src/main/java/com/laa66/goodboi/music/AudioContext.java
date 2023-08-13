package com.laa66.goodboi.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;


@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AudioContext {
    AudioPlayer player;
    TrackScheduler scheduler;
    TrackLoadResultHandler loadResultHandler;

    public static AudioContext createContext(AudioPlayer player) {
        TrackScheduler scheduler = new TrackScheduler();
        TrackLoadResultHandler loadResultHandler = new TrackLoadResultHandler(scheduler, player);
        return new AudioContext(player, scheduler, loadResultHandler);
    }
}
