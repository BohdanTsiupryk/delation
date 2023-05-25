package bts.delation.discord.listeners;

import bts.delation.discord.templates.ResponseTemplate;
import bts.delation.model.DiscordUser;
import bts.delation.model.Feedback;
import bts.delation.model.FeedbackType;
import bts.delation.model.Status;
import bts.delation.service.DiscordUserService;
import bts.delation.service.FeedbackService;
import discord4j.core.event.domain.interaction.ApplicationCommandInteractionEvent;
import discord4j.core.object.command.ApplicationCommandInteraction;
import discord4j.core.object.command.ApplicationCommandInteractionOption;
import discord4j.core.object.component.ActionRow;
import discord4j.core.object.component.Button;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.User;
import discord4j.core.spec.InteractionApplicationCommandCallbackSpec;
import discord4j.core.spec.MessageCreateSpec;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CommandListener implements DiscordEventListener<ApplicationCommandInteractionEvent> {

    private final FeedbackService feedbackService;
    private final DiscordUserService discordUserService;

    @Override
    public Class<ApplicationCommandInteractionEvent> getEventType() {
        return ApplicationCommandInteractionEvent.class;
    }

    @Override
    public Mono<Void> execute(ApplicationCommandInteractionEvent event) {

        if (!event.getCommandName().equals("feedback")) return Mono.empty();
        Member member = event.getInteraction().getMember().get();

        if (!discordUserService.checkUserAutorize(member.getId().asString(), member.getUsername())) {

            return event.reply(InteractionApplicationCommandCallbackSpec.builder()
                            .content(ResponseTemplate.UNLINKED)
                            .addComponent(ActionRow.of(Button.link("https://bcraft.fun/accounts/profile/", "Підключити")))
                    .build());
        }


        String author = member.getUsername();
        Optional<ApplicationCommandInteraction> commandInteraction = event.getInteraction().getCommandInteraction();

        FeedbackType type = FeedbackType.getTypeByValue(getOption(commandInteraction, "type"));
        String value = getOption(commandInteraction, "value");

        Feedback feedback = saveFeedbackToDb(event, author, type, value);

        return event.reply(String.format("%s прийнято, дякуємо що робите нас кращими \n |%s|", feedback.getType().getUa(), feedback.getText()));
    }

    private Feedback saveFeedbackToDb(ApplicationCommandInteractionEvent event, String author, FeedbackType type, String value) {
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
                .map(User::getUsername)
                .collect(Collectors.toSet());

        return feedbackService.save(new Feedback(
                UUID.randomUUID().toString(),
                discordAuthor,
                mentions,
                text.toString(),
                Status.NEW,
                type,
                LocalDateTime.now()
        ));
    }

    private String getOption(Optional<ApplicationCommandInteraction> commandInteraction, String value) {
        return commandInteraction
                .flatMap(ci -> ci.getOption(value))
                .flatMap(ApplicationCommandInteractionOption::getValue).get().getRaw();
    }
}
