package com.laa66.goodboi.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import lombok.extern.slf4j.Slf4j;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

@Slf4j
public class TrackScheduler extends AudioEventAdapter implements AudioEventProcessor {

    private final Queue<AudioTrack> trackQueue = new LinkedBlockingQueue<>();

    @Override
    public void onPlayerPause(AudioPlayer player) {
        log.info("Player paused!");
    }

    @Override
    public void onTrackStart(AudioPlayer player, AudioTrack track) {
        log.info("Track started! " + track.getInfo().title);
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        if (endReason == AudioTrackEndReason.FINISHED)
            player.playTrack(trackQueue.poll());
    }

    @Override
    public void stop(AudioPlayer player) {
        log.info("Player stopped!");
    }

    @Override
    public void pause(AudioPlayer player) {
        log.info("Player paused!");
    }

    @Override
    public void resume(AudioPlayer player) {
        log.info("Player resumed!");
    }

    @Override
    public void queue(AudioPlayer player, AudioTrack track) {
        trackQueue.offer(track);
        if (player.getPlayingTrack() == null) player.playTrack(trackQueue.poll());
    }

    @Override
    public void skip(AudioPlayer player) {
        if (!trackQueue.isEmpty() && player.getPlayingTrack() != null) {
            AudioTrack track = trackQueue.poll();
            player.playTrack(track);
        }
    }

    @Override
    public void clear() {
        log.info("Queue cleared!");
    }
}