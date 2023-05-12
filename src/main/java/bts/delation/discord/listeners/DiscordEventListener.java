package bts.delation.discord.listeners;

import discord4j.core.event.domain.Event;
import reactor.core.publisher.Mono;

public interface DiscordEventListener<T extends Event> {

    Class<T> getEventType();
    Mono<Void> execute(T event);

    default Mono<Void> handleError(Throwable error) {
        return Mono.empty();
    }
}
