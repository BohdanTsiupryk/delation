package bts.delation.discord.listeners;

import bts.delation.discord.listeners.commands.SlashCommand;
import bts.delation.discord.templates.ResponseTemplate;
import bts.delation.service.DiscordUserService;
import discord4j.common.util.Snowflake;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.component.ActionRow;
import discord4j.core.object.component.Button;
import discord4j.core.object.entity.Member;
import discord4j.core.spec.InteractionApplicationCommandCallbackSpec;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collection;

@Component
@RequiredArgsConstructor
public class CommandListener implements DiscordEventListener<ChatInputInteractionEvent > {

    private final DiscordUserService discordUserService;
    private final Collection<SlashCommand> commands;

    @Override
    public Class<ChatInputInteractionEvent> getEventType() {
        return ChatInputInteractionEvent.class;
    }

    @Override
    public Mono<Void> execute(ChatInputInteractionEvent event) {

        Member member = event.getInteraction().getUser().asMember(Snowflake.of(1106357010334744697L)).block();

        if (!discordUserService.checkUserAutorize(member.getId().asString(), member.getUsername())) {

            return event.reply(InteractionApplicationCommandCallbackSpec.builder()
                    .content(ResponseTemplate.UNLINKED)
                    .addComponent(ActionRow.of(Button.link("https://bcraft.fun/accounts/profile/", "Підключити")))
                    .build());
        }

        return Flux.fromIterable(commands)
                .filter(c -> c.getName().equals(event.getCommandName()))
                .next()
                .flatMap(c -> c.handle(event));
    }
}
