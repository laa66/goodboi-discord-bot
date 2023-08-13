package com.laa66.goodboi.command;

import com.laa66.goodboi.music.*;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.VoiceState;
import discord4j.core.object.entity.Member;
import discord4j.core.spec.VoiceChannelJoinSpec;
import discord4j.voice.AudioProvider;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.Optional;

@AllArgsConstructor
public class JoinCommand implements Command {

    private final MessageCreateEvent messageCreateEvent;
    private final AudioPlayerManager playerManager;
    private final AudioContextRepository contextRepository;

    @Override
    public Mono<Void> execute() {
        AudioProvider audioProvider = createAudioProvider();
        return Mono.justOrEmpty(messageCreateEvent.getMember())
                .flatMap(Member::getVoiceState)
                .flatMap(VoiceState::getChannel)
                .flatMap(voiceChannel -> voiceChannel.join(VoiceChannelJoinSpec.builder()
                        .provider(audioProvider)
                        .build()))
                .then();
    }

    //helpers
    private AudioProvider createAudioProvider() {
        long guildId = messageCreateEvent.getGuildId()
                .orElseThrow()
                .asLong();
        final AudioPlayer audioPlayer = Optional.ofNullable(contextRepository.getContext(guildId))
                .map(AudioContext::getPlayer)
                .orElseGet(() -> {
                    AudioPlayer player = playerManager.createPlayer();
                    AudioContext context = AudioContext.createContext(player);
                    contextRepository.saveContext(guildId, context);
                    return player;
                });
        return new LavaPlayerAudioProvider(audioPlayer);
    }

}
