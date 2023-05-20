package bts.delation.discord.listeners;

import bts.delation.exception.NotFoundException;
import bts.delation.model.Appeal;
import bts.delation.model.AppealStatus;
import bts.delation.repo.AppealRepo;
import bts.delation.service.AppealService;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static bts.delation.discord.templates.CommandTemplates.MOAN;
import static bts.delation.discord.templates.CommandTemplates.TODO;
import static bts.delation.discord.templates.ResponseTemplate.*;

@Service
@RequiredArgsConstructor
public class MessageCreateListener extends MessageListener implements DiscordEventListener<MessageCreateEvent> {

    private final AppealService appealService;
    private final String prefix = "!";


    @Override
    public Class<MessageCreateEvent> getEventType() {
        return MessageCreateEvent.class;
    }

    @Override
    public Mono<Void> execute(MessageCreateEvent event) {

        Message message = event.getMessage();
        String content = message.getContent();
        Optional<User> author = message.getAuthor();


        if (content.startsWith(prefix) && author.isPresent() && author.get().isBot()) {

            String command = content.contains(" ") ? content.substring(0, content.indexOf(" ")) : content;

            return switch (command) {
                case TODO -> processTodo(message);
                case MOAN -> processMoan(message);
                default -> processHelp(message);
            };
        }

        return Mono.empty();
    }

    private Mono<Void> processMoan(Message message) {

        String author = message.getAuthor()
                .orElseThrow(() -> new NotFoundException("Author don't present"))
                .getUsername();
        List<User> userMentions = message.getUserMentions();
        List<String> mentions = userMentions.stream()
                .map(User::getUsername)
                .toList();

        List<Pair<String, String>> idUsername = userMentions.stream()
                .map(u -> Pair.of(u.getId().asString(), u.getUsername()))
                .toList();

        StringBuilder sb = new StringBuilder(message.getContent());

        idUsername.forEach(s -> {
                    String str = "<@" + s.getFirst() + ">";
                    int start = sb.lastIndexOf(str);
                    sb.replace(start, start + str.length() , s.getSecond());
                });

        appealService.save(new Appeal(
                UUID.randomUUID().toString(),
                author,
                mentions,
                sb.substring(6),
                AppealStatus.NEW,
                LocalDateTime.now()
        ));

        return processCommand(message, MOAN_RESPONSE);
    }

    private Mono<Void> processHelp(Message message) {
        return processCommand(message, HELP_RESPONSE);
    }

    private Mono<Void> processTodo(Message message) {
        return processCommand(message, TODO_RESPONSE);
    }
}
