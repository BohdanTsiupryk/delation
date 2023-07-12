package bts.delation.discord;

import discord4j.core.object.command.ApplicationCommandInteraction;
import discord4j.core.object.command.ApplicationCommandInteractionOption;
import discord4j.core.object.entity.Member;

import java.util.Optional;
import java.util.function.Consumer;

public class DiscordUtils {

    public static String buildLinkPlaceholder(String placeholder, String link) {
        return "[" +
                placeholder +
                "](" +
                link +
                ")";
    }


    public static String publicFeedbackUrl(String host, String id) {

        return host + "/public/feedback/" + id;
    }

    public static String getOption(Optional<ApplicationCommandInteraction> commandInteraction, String value) {
        return commandInteraction
                .flatMap(ci -> ci.getOption(value))
                .flatMap(ApplicationCommandInteractionOption::getValue).get().getRaw();
    }

    public static String getAttachment(Optional<ApplicationCommandInteraction> commandInteraction, String attachment) {
        return commandInteraction.flatMap(ci -> ci.getOption(attachment))
                .flatMap(ApplicationCommandInteractionOption::getValue)
                .map(applicationCommandInteractionOptionValue -> applicationCommandInteractionOptionValue.asAttachment().getUrl())
                .orElse("");
    }

    public static Consumer<Member> replaceMembers(StringBuilder text) {
        return m -> {
            String toReplace = "<@" + m.getId().asString() + ">";
            int start = text.indexOf(toReplace);
            if (start == -1) return;
            text.replace(start, start + toReplace.length(), m.getUsername());
        };
    }
}
