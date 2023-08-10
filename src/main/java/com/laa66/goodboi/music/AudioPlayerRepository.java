package com.laa66.goodboi.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;

public interface AudioPlayerRepository {

    AudioPlayer getPlayer(String guildId);

    void savePlayer(String guildId, AudioPlayer player);

    void removePlayer(String guildId);

}
