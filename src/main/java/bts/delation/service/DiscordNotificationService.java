package bts.delation.service;

import bts.delation.model.Feedback;
import bts.delation.model.enums.Status;
import discord4j.common.util.Snowflake;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.object.component.ActionRow;
import discord4j.core.object.component.Button;
import discord4j.core.spec.MessageCreateSpec;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DiscordNotificationService {

    private final UserService userService;
    private final FeedbackService feedbackService;
    private final GatewayDiscordClient client;

    @Value("${host.base-url}")
    private String baseHostUrl;

    public void notifyAdmins(String guildId, String reason, Button... button) {

        Set<Snowflake> snowflakes = userService.findAdmins()
                .stream()
                .filter(u -> Objects.nonNull(u.getDiscordUser()))
                .map(a -> Snowflake.of(a.getDiscordUser().getId()))
                .collect(Collectors.toSet());

        client.getGuildById(Snowflake.of(guildId))
                .flatMap(g ->
                        g.requestMembers(snowflakes)
                                .flatMap(m -> m.getPrivateChannel().flatMap(c -> c.createMessage(MessageCreateSpec.builder()
                                                .addComponent(ActionRow.of(button))
                                                .content(reason)
                                        .build())))
                                .then()
                ).subscribe();
    }

    public void notifyTaskStatusChanged(Long feedbackId, Status newStatus) {
        Feedback feedback = feedbackService.getById(feedbackId);
        String url = baseHostUrl + "/moder/feedback/" + feedbackId;

        notifyAdmins(feedback.getGuildId(),
                String.format("Task %s was updated: %s", feedbackId, newStatus),
                Button.link(url, "Open")
        );
    }
}
