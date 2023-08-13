package com.laa66.goodboi.music;

public interface AudioContextRepository {

    AudioContext getContext(long guildId);

    void saveContext(long guildId, AudioContext context);

    void removeContext(long guildId);

}
