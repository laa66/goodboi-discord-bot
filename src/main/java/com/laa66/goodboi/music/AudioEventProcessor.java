package com.laa66.goodboi.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

public interface AudioEventProcessor {
    void stop(AudioPlayer player);
    void resume(AudioPlayer player);
    void queue(AudioPlayer player, AudioTrack track);
    void clear();
    void skip(AudioPlayer player);
}
