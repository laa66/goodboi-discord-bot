package com.laa66.goodboi.user;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("goodboi_db.users")
@Builder
@With
public class User {

    @Id
    private long id;

    @Column("guild_id")
    private long guildId;

    @Column("discord_id")
    private long discordId;

    private String username;

    @Column("warn_count")
    private int warnCount;

    private boolean banned;

}
