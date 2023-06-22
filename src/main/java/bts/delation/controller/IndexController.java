package bts.delation.controller;

import discord4j.common.util.Snowflake;
import discord4j.core.GatewayDiscordClient;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/index")
@RequiredArgsConstructor
public class IndexController {

    private final GatewayDiscordClient client;

    @GetMapping
    public String sss() {
        return "index";
    }
}
