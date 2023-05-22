package bts.delation.discord.listeners;

import bts.delation.discord.templates.CommandTemplates;
import bts.delation.discord.templates.ResponseTemplate;
import bts.delation.service.DiscordUserService;
import bts.delation.service.FeedbackService;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.component.Button;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MessageCreateListener extends MessageListener implements DiscordEventListener<MessageCreateEvent> {

    private final FeedbackService feedbackService;
    private final DiscordUserService discordUserService;

    private static final String prefix = "!";


    @Override
    public Class<MessageCreateEvent> getEventType() {
        return MessageCreateEvent.class;
    }

    @Override
    public Mono<Void> execute(MessageCreateEvent event) {

        Message message = event.getMessage();
        Optional<User> author = message.getAuthor();
        String content = message.getContent();

        if (!content.startsWith(prefix)) return Mono.empty();
        if (author.isEmpty() || author.get().isBot()) return Mono.empty();
        if (!isDiscordMineSync(author.get())) {
            return processUnlinkedAccount(message);
        }

        String command = content.contains(" ") ? content.substring(0, content.indexOf(" ")) : content;

        return switch (command) {
            case CommandTemplates.FEEDBACK -> processFeedback(message);
            case CommandTemplates.MENU -> processMenu(message);
            default -> processHelp(message);
        };
    }

    private Mono<Void> processMenu(Message message) {
        return null;
    }

    private boolean isDiscordMineSync(User author) {




        return true;
    }

    private Mono<Void> processFeedback(Message message) {


        return processCommand(message, ResponseTemplate.MOAN);
    }

    private Mono<Void> processHelp(Message message) {
        return processCommand(message, ResponseTemplate.HELP);
    }

    private Mono<Void> processUnlinkedAccount(Message message) {
        return processCommand(message, ResponseTemplate.UNLINKED, Button.link("https://bcraft.fun/accounts/profile/", "Підключити"));
    }
}
