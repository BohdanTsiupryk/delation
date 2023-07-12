package bts.delation.discord.listeners;

import discord4j.common.util.Snowflake;
import discord4j.core.event.domain.interaction.ButtonInteractionEvent;
import discord4j.core.object.entity.channel.MessageChannel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import discord4j.core.object.entity.Message;

//
//@Service
//@RequiredArgsConstructor
//public class ButtonListener implements DiscordEventListener<ButtonInteractionEvent> {
//
//    @Override
//    public Class<ButtonInteractionEvent> getEventType() {
//        return ButtonInteractionEvent.class;
//    }
//
//    @Override
//    public Mono<Void> execute(ButtonInteractionEvent event) {
//        return Mono.justOrEmpty(event.getInteraction().getMessage())
//                .map(Message::getId)
//                .then(event.edit(spec -> spec.setContent("misha hui")));
//
//    }
//}
