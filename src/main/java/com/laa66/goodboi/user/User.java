package com.laa66.goodboi.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("users")
public class User {

    @Id
    private long id;

    @Column("discord_id")
    private long discordId;

    private String username;

    @Column("warn_count")
    private int warnCount;

    private boolean banned;

}
