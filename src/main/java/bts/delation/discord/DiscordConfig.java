package bts.delation.discord;

import bts.delation.discord.listeners.DiscordEventListener;
import bts.delation.model.FeedbackType;
import discord4j.common.util.Snowflake;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.Event;
import discord4j.core.object.command.ApplicationCommandOption;
import discord4j.discordjson.json.*;
import discord4j.gateway.intent.IntentSet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Configuration
public class DiscordConfig {

    @Bean
    public <T extends Event> GatewayDiscordClient gatewayDiscordClient(List<DiscordEventListener<T>> listeners) {
        GatewayDiscordClient client = DiscordClientBuilder
                .create("MTEwNjIzNTQwMjA4MTI4ODIxMg.GO3dgn.aDSLkgZTdOgmP65ejVmLlyO5weSVMuC6jUDPRU")
                .build()
                .gateway()
                .setEnabledIntents(IntentSet.all())
                .login().block();

        Long appId = client.getRestClient().getApplicationId().block();

        ApplicationCommandRequest randomCommand = ApplicationCommandRequest.builder()
                .name("feedback")
                .description("Send feedback")
                .addOption(options())
                .addOption(ApplicationCommandOptionData.builder()
                        .name("value")
                        .description("ваш відгук\\скарга")
                        .type(ApplicationCommandOption.Type.STRING.getValue())
                        .required(true)
                        .build())
                .build();

        client.getRestClient().getApplicationService()
                .createGuildApplicationCommand(appId, 1106357010334744697L,randomCommand)
                .subscribe();


        listeners.forEach(listener -> {
            client.on(listener.getEventType())
                    .flatMap(listener::execute)
                    .onErrorResume(listener::handleError)
                    .subscribe();
        });

        return client;
    }

    private static ImmutableApplicationCommandOptionData options() {

        List<ApplicationCommandOptionChoiceData> collect = Arrays.stream(FeedbackType.values())
                .map(f -> ApplicationCommandOptionChoiceData.builder()
                        .name(f.getUa())
                        .value(f.getValue())
                        .build()
                ).collect(Collectors.toList());


        return ApplicationCommandOptionData.builder()
                .name("type")
                .description("feedback type")
                .type(ApplicationCommandOption.Type.STRING.getValue())
                .addAllChoices(collect)
                .required(true)
                .build();
    }


}
