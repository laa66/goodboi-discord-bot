package com.laa66.goodboi.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TrackScheduler extends AudioEventAdapter {

    @Override
    public void onPlayerPause(AudioPlayer player) {
        log.info("Track paused! ");
    }

    @Override
    public void onTrackStart(AudioPlayer player, AudioTrack track) {
        log.info("Track started! " + track.getInfo().title);
    }

    public void queue(AudioPlayer player, AudioTrack track) {
        log.info("Track queued! " + track.getInfo().title);
    }

}
