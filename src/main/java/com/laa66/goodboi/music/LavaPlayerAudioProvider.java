package com.laa66.goodboi.music;

import com.sedmelluq.discord.lavaplayer.format.StandardAudioDataFormats;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.playback.MutableAudioFrame;
import discord4j.voice.AudioProvider;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class LavaPlayerAudioProvider extends AudioProvider {
    private final AudioPlayer audioPlayer;
    private final MutableAudioFrame frame = new MutableAudioFrame();

    public LavaPlayerAudioProvider(final AudioPlayer player) {
        super(ByteBuffer.allocate(StandardAudioDataFormats.DISCORD_OPUS.maximumChunkSize()));
        this.audioPlayer = player;
    }

    @Override
    public boolean provide() {
        final boolean isProvide = audioPlayer.provide(frame);
        if (isProvide) getBuffer().flip();
        return isProvide;
    }
}
