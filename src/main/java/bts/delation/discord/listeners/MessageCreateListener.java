package bts.delation.discord.listeners;

import bts.delation.discord.templates.CommandTemplates;
import bts.delation.discord.templates.ResponseTemplate;
import bts.delation.service.DiscordUserService;
import bts.delation.service.FeedbackService;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.component.Button;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.User;
import discord4j.core.spec.MessageCreateSpec;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Optional;

////@Service
//@RequiredArgsConstructor
//public class MessageCreateListener extends MessageListener implements DiscordEventListener<MessageCreateEvent> {
//
//    private final FeedbackService feedbackService;
//    private final DiscordUserService discordUserService;
//
//    private static final String prefix = "!";
//
//
//    @Override
//    public Class<MessageCreateEvent> getEventType() {
//        return MessageCreateEvent.class;
//    }
//
//    @Override
//    public Mono<Void> execute(MessageCreateEvent event) {
//
//        Message message = event.getMessage();
//        if (message.getContent().equalsIgnoreCase("дякую")) {
//            event.getMember().get().getPrivateChannel()
//                    .flatMap(c -> c.createMessage(MessageCreateSpec.builder()
//                            .content("Догори сракою =)")
//                            .build()))
//                    .subscribe();
//        }
//        return Mono.empty();
//    }
//
//}
