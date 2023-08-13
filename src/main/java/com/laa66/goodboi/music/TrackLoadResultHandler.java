package com.laa66.goodboi.music;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public class TrackLoadResultHandler implements AudioLoadResultHandler {

    private final TrackScheduler scheduler;
    private final AudioPlayer player;

    @Override
    public void trackLoaded(AudioTrack track) {
        scheduler.queue(player, track);
    }

    @Override
    public void playlistLoaded(AudioPlaylist playlist) {

    }

    @Override
    public void noMatches() {
        log.info("No matches found!");
    }

    @Override
    public void loadFailed(FriendlyException exception) {
        log.info("Load failed!", exception);
    }
}
