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
        ApplicationCommandRequest helpCommand = createHelpCommand();
        ImmutableApplicationCommandRequest syncCommand = createSyncCommand();

        client.getRestClient().getApplicationService()
                .bulkOverwriteGlobalApplicationCommand(
                        appId,
                        List.of(feedbackCommand, statusCommand, syncCommand, helpCommand)
                )
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
                .description("Надіслати відгук")
                .addOption(optionFeedbackType())
                .addOption(ApplicationCommandOptionData.builder()
                        .name("value")
                        .description("Тут натикайте, що думаєте")
                        .type(ApplicationCommandOption.Type.STRING.getValue())
                        .required(true)
                        .build())
                .addOption(ApplicationCommandOptionData.builder()
                        .name("attachment")
                        .description("Тут виберіть фото з вашим доказом (необов'язково)")
                        .type(ApplicationCommandOption.Type.ATTACHMENT.getValue())
                        .required(false)
                        .build())
                .build();
    }

    private static ImmutableApplicationCommandRequest createHelpCommand() {
        return ApplicationCommandRequest.builder()
                .name("help")
                .description("Допомога")
                .build();
    }

    private static ImmutableApplicationCommandRequest createStatusCommand() {
        return ApplicationCommandRequest.builder()
                .name("status")
                .description("Перевірити чи на ваш відгук нам не начхати")
                .build();
    }

    private static ImmutableApplicationCommandRequest createSyncCommand() {
        return ApplicationCommandRequest.builder()
                .name("sync")
                .description("Синхронізувати аккаунт з діскордом")
                .addOption(ApplicationCommandOptionData.builder()
                        .name("code")
                        .description("Ваш код")
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
                .description("Тип відгуку, правильність вказання, зменшує час на її розгляд")
                .type(ApplicationCommandOption.Type.STRING.getValue())
                .addAllChoices(collect)
                .required(true)
                .build();
    }
}
