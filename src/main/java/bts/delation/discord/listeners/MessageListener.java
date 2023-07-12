package bts.delation.discord.listeners;

import discord4j.core.object.component.ActionRow;
import discord4j.core.object.component.Button;
import discord4j.core.object.entity.Message;
import discord4j.core.spec.MessageCreateSpec;
import reactor.core.publisher.Mono;

//public abstract class MessageListener {
//
//    public Mono<Void> processCommand(Message eventMessage, String text, Button... buttons) {
//        return Mono.just(eventMessage)
//          .filter(message -> message.getAuthor().map(user -> !user.isBot()).orElse(false))
//          .flatMap(Message::getChannel)
//          .flatMap(channel -> channel.createMessage(
//                  MessageCreateSpec.builder()
//                          .content(text)
//                          .addComponent(ActionRow.of(buttons))
//                          .build()
//          ))
//          .then();
//    }
//}