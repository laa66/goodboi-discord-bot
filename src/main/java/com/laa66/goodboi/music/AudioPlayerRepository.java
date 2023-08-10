package com.laa66.goodboi.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;

public interface AudioPlayerRepository {

    AudioPlayer getPlayer(long guildId);

    void savePlayer(long guildId, AudioPlayer player);

    void removePlayer(long guildId);

}
