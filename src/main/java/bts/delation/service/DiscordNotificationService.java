package bts.delation.service;

import bts.delation.model.Feedback;
import discord4j.common.util.Snowflake;
import discord4j.core.GatewayDiscordClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DiscordNotificationService {

    private final DiscordUserService discordUserService;
    private final UserService userService;
    private final FeedbackService feedbackService;
    private final GatewayDiscordClient client;

    private static final String GUILD_ID = "1106357010334744697";

    public void notifyAdmins(String reason) {

        Set<Snowflake> snowflakes = userService.findAdmins()
                .stream()
                .filter(u -> Objects.nonNull(u.getDiscordUser()))
                .map(a -> Snowflake.of(a.getDiscordUser().getId()))
                .collect(Collectors.toSet());

        client.getGuildById(Snowflake.of(GUILD_ID))
                .flatMap(g ->
                        g.requestMembers(snowflakes)
                                .flatMap(m -> m.getPrivateChannel().flatMap(c -> c.createMessage(reason)))
                                .then()
                ).subscribe();
    }

    public void notifyTaskStatusChanged(String feedbackId, String newStatus) {
        Feedback feedback = feedbackService.getById(feedbackId);

        notifyAdmins(String.format("Task %s was updated: %s", feedback.getId(), newStatus));
    }

    public void notifyAdminModerAppeal(String feedbackId) {
        notifyAdmins("New appeal on moderator");
    }
}
