package bts.delation.discord;

import bts.delation.discord.listeners.DiscordEventListener;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.Event;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class DiscordConfig {

    @Bean
    public <T extends Event> GatewayDiscordClient gatewayDiscordClient(List<DiscordEventListener<T>> listeners) {
        GatewayDiscordClient client = DiscordClientBuilder
                .create("MTEwNjIzNTQwMjA4MTI4ODIxMg.GO3dgn.aDSLkgZTdOgmP65ejVmLlyO5weSVMuC6jUDPRU")
                .build()
                .login().block();

        listeners.forEach(listener -> {
            client.on(listener.getEventType())
                    .flatMap(listener::execute)
                    .onErrorResume(listener::handleError)
                    .subscribe();
        });

        return client;
    }



}
