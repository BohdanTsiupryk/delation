package bts.delation.discord.listeners.commands;

import bts.delation.discord.DiscordUtils;
import bts.delation.discord.templates.ResponseTemplate;
import bts.delation.model.Feedback;
import bts.delation.service.FeedbackService;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class StatusCommand implements SlashCommand {

    private final FeedbackService feedbackService;

    @Value("${host.base-url}")
    private String baseHostUrl;

    @Override
    public String getName() {
        return "status";
    }

    @Override
    public Mono<Void> handle(ChatInputInteractionEvent event) {

        String userID = event.getInteraction().getUser().getId().asString();
        List<Feedback> feedbacks = feedbackService.getByAuthor(userID);

        StringBuilder sb = new StringBuilder(ResponseTemplate.STATUS_TASK_LIST);
        feedbacks.forEach(f -> sb
                .append(
                        DiscordUtils.buildLinkPlaceholder(f.getId().toString(), DiscordUtils.publicFeedbackUrl(baseHostUrl, f.getId().toString()))
                )
                .append(" | ")
                .append(f.getStatus())
                .append(" | ")
                .append(trimStatusText(f.getText()))
                .append("\n")
        );

        return event.reply(sb.toString()).withEphemeral(true);
    }

    private static String trimStatusText(String text) {
        return text.length() > 32 ? text.substring(0, 30) : text;
    }
}