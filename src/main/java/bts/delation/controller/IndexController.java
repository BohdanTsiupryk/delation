package bts.delation.controller;

import discord4j.common.util.Snowflake;
import discord4j.core.GatewayDiscordClient;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class IndexController {

    private final GatewayDiscordClient client;

    @GetMapping("/sss")
    public String sss() {


        client.getChannelById(Snowflake.of("1109648083119247530"))
                .flatMap(c -> c.getRestChannel().createMessage("asd"))
                .subscribe();


        return "index";
    }
}
