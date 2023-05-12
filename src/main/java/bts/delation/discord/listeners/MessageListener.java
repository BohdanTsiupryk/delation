package bts.delation.discord.listeners;

import discord4j.core.object.entity.Message;
import reactor.core.publisher.Mono;

public abstract class MessageListener {

    public Mono<Void> processCommand(Message eventMessage, String text) {
        return Mono.just(eventMessage)
          .filter(message -> message.getAuthor().map(user -> !user.isBot()).orElse(false))
          .flatMap(Message::getChannel)
          .flatMap(channel -> channel.createMessage(text))
          .then();
    }
}