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

    public void notifyAdmins(String feedbackId, String reason) {

        Feedback feedback = feedbackService.getById(feedbackId);
        Set<Snowflake> snowflakes = userService.findAdmins()
                .stream()
                .filter(u -> Objects.nonNull(u.getDiscordUser()))
                .map(a -> Snowflake.of(a.getDiscordUser().getId()))
                .collect(Collectors.toSet());

        client.getGuildById(Snowflake.of(GUILD_ID))
                .flatMap(g ->
                        g.requestMembers(snowflakes)
                                .flatMap(m -> m.getPrivateChannel().flatMap(c -> c.createMessage(String.format("Task %s was updated: %s", feedback.getId(), reason))))
                                .then()
                ).subscribe();
    }
}
