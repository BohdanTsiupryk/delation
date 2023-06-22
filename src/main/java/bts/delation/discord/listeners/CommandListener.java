package bts.delation.discord.listeners;

import bts.delation.discord.templates.ResponseTemplate;
import bts.delation.model.DiscordUser;
import bts.delation.model.Feedback;
import bts.delation.model.SyncCode;
import bts.delation.model.User;
import bts.delation.model.enums.FeedbackType;
import bts.delation.model.enums.Status;
import bts.delation.service.*;
import discord4j.common.util.Snowflake;
import discord4j.core.event.domain.interaction.ApplicationCommandInteractionEvent;
import discord4j.core.object.command.ApplicationCommandInteraction;
import discord4j.core.object.command.ApplicationCommandInteractionOption;
import discord4j.core.object.component.ActionRow;
import discord4j.core.object.component.Button;
import discord4j.core.object.entity.Member;
import discord4j.core.spec.InteractionApplicationCommandCallbackSpec;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CommandListener implements DiscordEventListener<ApplicationCommandInteractionEvent> {

    private final FeedbackService feedbackService;
    private final DiscordUserService discordUserService;
    private final UserService userService;
    private final HistoryService historyService;
    private final SyncService syncService;

    @Override
    public Class<ApplicationCommandInteractionEvent> getEventType() {
        return ApplicationCommandInteractionEvent.class;
    }

    @Override
    public Mono<Void> execute(ApplicationCommandInteractionEvent event) {

        Member member = event.getInteraction().getUser().asMember(Snowflake.of(1106357010334744697L)).block();

        if (!discordUserService.checkUserAutorize(member.getId().asString(), member.getUsername())) {

            return event.reply(InteractionApplicationCommandCallbackSpec.builder()
                    .content(ResponseTemplate.UNLINKED)
                    .addComponent(ActionRow.of(Button.link("https://bcraft.fun/accounts/profile/", "Підключити")))
                    .build());
        }

        if (event.getCommandName().equals("feedback")) return processFeedback(event, member);
        if (event.getCommandName().equals("status")) return processStatus(event, member);
        if (event.getCommandName().equals("sync")) return processSync(event, member);

        return Mono.empty();
    }

    private Mono<Void> processSync(ApplicationCommandInteractionEvent event, Member member) {

        Optional<ApplicationCommandInteraction> commandInteraction = event.getInteraction().getCommandInteraction();
        String code = getOption(commandInteraction, "code");

        SyncCode syncCode = syncService.getByCode(code);

        User user = userService.getById(syncCode.getUserId());

        DiscordUser discordUser = discordUserService.getById(member.getId().asString());
        user.setDiscordUser(discordUser);

        userService.save(user);
        return event.reply("OK");
    }

    private Mono<Void> processStatus(ApplicationCommandInteractionEvent event, Member member) {

        List<Feedback> feedbacks = feedbackService.getByAuthor(member.getId().asString());

        StringBuilder sb = new StringBuilder();
        feedbacks.forEach(f -> sb.append(f.getId()).append("|").append(f.getStatus()).append("|").append(f.getText()).append("\n"));

        return event.reply(String.format("Task list: \n %s", sb));
    }

    private Mono<Void> processFeedback(ApplicationCommandInteractionEvent event, Member member) {

        String author = member.getUsername();
        Optional<ApplicationCommandInteraction> commandInteraction = event.getInteraction().getCommandInteraction();

        FeedbackType type = FeedbackType.getTypeByValue(getOption(commandInteraction, "type"));
        String value = getOption(commandInteraction, "value");
        String url = getAttachment(commandInteraction, "attachment");
        Feedback feedback = saveFeedbackToDb(event, author, type, value, url);

        historyService.taskCreated(feedback, author);

        return event.reply(String.format("%s прийнято, дякуємо що робите нас кращими \n |%s|", feedback.getType().getUa(), feedback.getText()));
    }

    private String getAttachment(Optional<ApplicationCommandInteraction> commandInteraction, String attachment) {
        return commandInteraction.flatMap(ci -> ci.getOption(attachment))
                .flatMap(ApplicationCommandInteractionOption::getValue)
                .map(applicationCommandInteractionOptionValue -> applicationCommandInteractionOptionValue.asAttachment().getUrl())
                .orElse("");
    }

    private String getOption(Optional<ApplicationCommandInteraction> commandInteraction, String value) {
        return commandInteraction
                .flatMap(ci -> ci.getOption(value))
                .flatMap(ApplicationCommandInteractionOption::getValue).get().getRaw();
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
                .stream().peek(m -> {
                    String toReplace = "<@" + m.getId().asString() + ">";
                    int start = text.indexOf(toReplace);
                    if (start == -1) return;
                    text.replace(start, start + toReplace.length(), m.getUsername());
                })
                .map(member -> member.getUsername())
                .collect(Collectors.toSet());

        return feedbackService.save(new Feedback(
                UUID.randomUUID().toString(),
                discordAuthor,
                mentions,
                text.toString(),
                Status.NEW,
                url,
                type,
                LocalDateTime.now()
        ));
    }
}
