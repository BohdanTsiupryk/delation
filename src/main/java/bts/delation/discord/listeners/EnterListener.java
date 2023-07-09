package bts.delation.discord.listeners;

import bts.delation.discord.templates.ResponseTemplate;
import bts.delation.service.DiscordUserService;
import discord4j.core.event.domain.guild.MemberJoinEvent;
import discord4j.core.object.entity.Member;
import discord4j.core.spec.MessageCreateSpec;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class EnterListener implements DiscordEventListener<MemberJoinEvent> {

    private final DiscordUserService discordUserService;

    @Override
    public Class<MemberJoinEvent> getEventType() {
        return MemberJoinEvent.class;
    }

    @Override
    public Mono<Void> execute(MemberJoinEvent event) {

        Member member = event.getMember();
        member.getPrivateChannel()
                .flatMap(c -> c.createMessage(MessageCreateSpec.builder()
                        .content(ResponseTemplate.HELP)
                        .build()))
                .subscribe();

        discordUserService.saveAndSync(member.getId().asString(), member.getUsername());

        return Mono.empty();
    }
}
