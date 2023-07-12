package bts.delation.discord.listeners.commands;

import bts.delation.discord.templates.ResponseTemplate;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class HelpCommand implements SlashCommand {
    @Override
    public String getName() {
        return "help";
    }

    @Override
    public Mono<Void> handle(ChatInputInteractionEvent event) {

        return event.reply()
            .withContent(ResponseTemplate.HELP);
    }
}