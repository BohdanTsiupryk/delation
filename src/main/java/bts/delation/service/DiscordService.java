package bts.delation.service;

import bts.delation.model.DiscordUser;
import discord4j.common.util.Snowflake;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class DiscordService {

    private final DiscordUserService discordUserService;

    @Async
    public void saveUsersToDb(String guildId) {

//
//        client.getGuildById(Snowflake.of(guildId))
//                .subscribe(
//                        g -> {
//                            ArrayList<DiscordUser> discordUsers = new ArrayList<>();
//
//                            g.getMembers()
//                                    .filter(m -> !m.isBot())
//                                    .subscribe(member -> discordUsers.add(new DiscordUser(member.getId().asString(), member.getUsername())));
//
//                            for (DiscordUser d: discordUsers) {
//                                discordUserService.saveAndSync(d.getId(), d.getDiscordUsername());
//                            }
//                        }
//                );


    }
}
