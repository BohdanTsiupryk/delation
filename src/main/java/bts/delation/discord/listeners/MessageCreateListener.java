package bts.delation.discord.listeners;

import bts.delation.model.Appeal;
import bts.delation.repo.AppealRepo;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;


import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static bts.delation.discord.templates.CommandTemplates.MOAN;
import static bts.delation.discord.templates.CommandTemplates.TODO;
import static bts.delation.discord.templates.ResponseTemplate.MOAN_RESPONSE;
import static bts.delation.discord.templates.ResponseTemplate.TODO_RESPONSE;

@Service
@RequiredArgsConstructor
public class MessageCreateListener extends MessageListener implements DiscordEventListener<MessageCreateEvent> {

    private final AppealRepo appealRepo;


    @Override
    public Class<MessageCreateEvent> getEventType() {
        return MessageCreateEvent.class;
    }

    @Override
    public Mono<Void> execute(MessageCreateEvent event) {

        Message message = event.getMessage();
        String content = message.getContent();

        Mono<Void> mono = Mono.empty();

        if (!event.getMessage().getAuthor().get().isBot() && content.startsWith("!")) {

            String command = content.contains(" ") ? content.substring(0, content.indexOf(" ")) : content;

            mono = switch (command) {
                case TODO -> processTodo(message);
                case MOAN -> processMoan(message);
                default -> processHelp(message);
            };
        }

        return mono;
    }

    private Mono<Void> processMoan(Message message) {

        String author = message.getAuthor().get().getUsername();
        List<User> userMentions = message.getUserMentions();
        List<String> mentions = userMentions.stream()
                .map(User::getUsername)
                .toList();

        List<Pair<String, String>> collect = userMentions.stream()
                .map(u -> Pair.of(u.getId().asString(), u.getUsername()))
                .toList();

        StringBuilder sb = new StringBuilder(message.getContent());

        collect.forEach(s -> {
                    String str = "<@" + s.getFirst() + ">";
                    int start = sb.lastIndexOf(str);
                    sb.replace(start, start + str.length() , s.getSecond());
                });

        appealRepo.save(new Appeal(
                UUID.randomUUID().toString(),
                author,
                mentions,
                sb.substring(6).toString()
        ));

        return processCommand(message, MOAN_RESPONSE);
    }

    private Mono<Void> processHelp(Message message) {
        return null;
    }

    private Mono<Void> processTodo(Message message) {
        return processCommand(message, TODO_RESPONSE);
    }
}
