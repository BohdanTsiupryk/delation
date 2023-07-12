package bts.delation.discord.listeners.commands;

import bts.delation.discord.templates.ResponseTemplate;
import bts.delation.model.DiscordUser;
import bts.delation.model.Feedback;
import bts.delation.model.enums.FeedbackType;
import bts.delation.model.enums.Status;
import bts.delation.service.DiscordUserService;
import bts.delation.service.FeedbackService;
import bts.delation.service.HistoryService;
import discord4j.core.event.domain.interaction.ApplicationCommandInteractionEvent;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.command.ApplicationCommandInteraction;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static bts.delation.discord.DiscordUtils.*;

@Component
@RequiredArgsConstructor
public class FeedbackCommand implements SlashCommand {

    private final FeedbackService feedbackService;
    private final DiscordUserService discordUserService;
    private final HistoryService historyService;

    @Value("${host.base-url}")
    private String baseHostUrl;

    @Override
    public String getName() {
        return "feedback";
    }

    @Override
    public Mono<Void> handle(ChatInputInteractionEvent event) {

        String author = event.getInteraction().getUser().getUsername();
        Optional<ApplicationCommandInteraction> commandInteraction = event.getInteraction().getCommandInteraction();

        FeedbackType type = FeedbackType.getTypeByValue(getOption(commandInteraction, "type"));
        String value = getOption(commandInteraction, "value");
        String url = getAttachment(commandInteraction, "attachment");

        Feedback feedback = saveFeedbackToDb(event, author, type, value, url);

        historyService.taskCreated(feedback, author);

        return event.reply(String.format(ResponseTemplate.FEEDBACK_RESPONSE, feedback.getType().getUa(), publicFeedbackUrl(baseHostUrl, feedback.getId().toString())))
                .withEphemeral(true);
    }

    private Feedback saveFeedbackToDb(ApplicationCommandInteractionEvent event, String author, FeedbackType type, String value, String url) {
        Set<String> mentionsIds = new HashSet<>();
        StringBuilder text = new StringBuilder(value);

        DiscordUser discordAuthor = discordUserService.getByUsername(author);

        int lastIndex = 0;
        while (text.indexOf("<@", lastIndex) != -1) {
            String id = text.substring(text.indexOf("<@", lastIndex) + 2, text.indexOf(">", text.indexOf("<@", lastIndex) + 2));
            mentionsIds.add(id);

            lastIndex = text.indexOf("<@", lastIndex) + 1;
        }

        Set<String> mentions = event.getInteraction()
                .getGuild()
                .block()
                .getMembers()
                .filter(m -> mentionsIds.contains(m.getId().asString()))
                .collectList()
                .block()
                .stream()
                .peek(replaceMembers(text))
                .map(User::getUsername)
                .collect(Collectors.toSet());

        return feedbackService.save(new Feedback(
                discordAuthor,
                mentions,
                text.toString(),
                Status.NEW,
                url,
                type,
                LocalDateTime.now(),
                event.getInteraction().getGuildId().get().asString()
        ));
    }
}