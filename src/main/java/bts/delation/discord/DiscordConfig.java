package bts.delation.discord;

import bts.delation.discord.listeners.DiscordEventListener;
import bts.delation.model.enums.FeedbackType;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.Event;
import discord4j.core.object.command.ApplicationCommandOption;
import discord4j.discordjson.json.*;
import discord4j.gateway.intent.IntentSet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class DiscordConfig {

    @Bean
    public <T extends Event> GatewayDiscordClient gatewayDiscordClient(
            List<DiscordEventListener<T>> listeners,
            @Value("${discord.key}") String discordKey
    ) {
        GatewayDiscordClient client = DiscordClientBuilder
                .create(discordKey)
                .build()
                .gateway()
                .setEnabledIntents(IntentSet.all())
                .login().block();

        Long appId = client.getRestClient().getApplicationId().block();

        ApplicationCommandRequest feedbackCommand = createFeedbackCommand();
        ApplicationCommandRequest statusCommand = createStatusCommand();
        ImmutableApplicationCommandRequest syncCommand = createSyncCommand();

        client.getRestClient().getApplicationService()
                .bulkOverwriteGlobalApplicationCommand(appId, List.of(feedbackCommand, statusCommand, syncCommand))
                .subscribe();

        listeners.forEach(listener -> {
            client.on(listener.getEventType())
                    .flatMap(listener::execute)
                    .onErrorResume(listener::handleError)
                    .subscribe();
        });

        return client;
    }

    private static ImmutableApplicationCommandRequest createFeedbackCommand() {
        return ApplicationCommandRequest.builder()
                .name("feedback")
                .description("Send feedback")
                .addOption(optionFeedbackType())
                .addOption(ApplicationCommandOptionData.builder()
                        .name("value")
                        .description("ваш відгук/скарга")
                        .type(ApplicationCommandOption.Type.STRING.getValue())
                        .required(true)
                        .build())
                .addOption(ApplicationCommandOptionData.builder()
                        .name("attachment")
                        .description("додатки")
                        .type(ApplicationCommandOption.Type.ATTACHMENT.getValue())
                        .required(false)
                        .build())
                .build();
    }

    private static ImmutableApplicationCommandRequest createStatusCommand() {
        return ApplicationCommandRequest.builder()
                .name("status")
                .description("Check feedback statuses")
                .build();
    }

    private static ImmutableApplicationCommandRequest createSyncCommand() {
        return ApplicationCommandRequest.builder()
                .name("sync")
                .description("Sync acc with discord")
                .addOption(ApplicationCommandOptionData.builder()
                        .name("code")
                        .description("Your code")
                        .type(ApplicationCommandOption.Type.STRING.getValue())
                        .required(true)
                        .build())
                .build();
    }

    private static ImmutableApplicationCommandOptionData optionFeedbackType() {

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
