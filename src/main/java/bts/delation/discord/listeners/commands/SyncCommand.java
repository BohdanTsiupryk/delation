package bts.delation.discord.listeners.commands;

import bts.delation.model.DiscordUser;
import bts.delation.service.DiscordUserService;
import bts.delation.service.SyncService;
import bts.delation.service.UserService;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.command.ApplicationCommandInteraction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Optional;

import static bts.delation.discord.DiscordUtils.getOption;

@Component
@RequiredArgsConstructor
public class SyncCommand implements SlashCommand {

    private final SyncService syncService;
    private final UserService userService;
    private final DiscordUserService discordUserService;

    @Override
    public String getName() {
        return "sync";
    }

    @Override
    public Mono<Void> handle(ChatInputInteractionEvent event) {

        Optional<ApplicationCommandInteraction> commandInteraction = event.getInteraction().getCommandInteraction();
        String code = getOption(commandInteraction, "code");

        syncService.getByCode(code)
                .ifPresent(c -> {
                    userService.getByIdOpt(c.getUserId())
                            .ifPresent(user -> {
                                DiscordUser discordUser = discordUserService.getById(event.getInteraction().getUser().getId().asString());
                                user.setDiscordUser(discordUser);

                                userService.save(user);
                                syncService.remove(c);
                            });
                });

        return event.reply("OK").withEphemeral(true);
    }
}