package bts.delation.service;

import bts.delation.exception.NotFoundException;
import bts.delation.model.DiscordUser;
import bts.delation.repo.DiscordUserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DiscordUserService {

    private final DiscordUserRepo discordUserRepo;
    private final RestTemplate restTemplate;

    public DiscordUser save(DiscordUser discordUser) {

        return discordUserRepo.save(discordUser);
    }

    public boolean checkUserAutorize(String id,String name) {

        Optional<DiscordUser> byId = discordUserRepo.findById(id);

        if (byId.isEmpty()) return syncUserWithMine(id, name);
        DiscordUser discordUser = byId.get();
        if (discordUser.isSyncWithMine()) return true;

        try {
            BandercraftUser userFromMine = getUserFromMine(id);
            discordUser.setMineUsername(userFromMine.username);
            discordUser.setSyncWithMine(true);

            discordUserRepo.save(discordUser);

            return true;
        } catch (NotFoundException e) {
            return false;
        }
    }

    public DiscordUser getByUsername(String username) {
        return discordUserRepo.findDiscordUserByDiscordUsername(username)
                .orElseThrow(() -> new NotFoundException("Discord user not found"));
    }

    private boolean syncUserWithMine(String id, String name) {

        BandercraftUser userFromMine;
        try {
            userFromMine = getUserFromMine(id);
        } catch (NotFoundException e) {

            discordUserRepo.save(new DiscordUser(id, name));

            return false;
        }

        discordUserRepo.save(new DiscordUser(
                id,
                name,
                userFromMine.username,
                true
        ));
        return true;
    }

    private BandercraftUser getUserFromMine(String id) {
        String baseUrl = "https://bcraft.fun/api/app/v1/discord/user/";

        String url = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .queryParam("discord_id", id)
                .toUriString();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("App-Token", "8eca87b426d8477eadcf1bdd1656441b");
        HttpEntity<BandercraftUser> http = new HttpEntity<>(httpHeaders);

        ResponseEntity<Wrap> exchange = restTemplate.exchange(url, HttpMethod.GET, http, Wrap.class);

        if (exchange.getStatusCode().isError()) throw new NotFoundException("User not found on bandercraft");

        return exchange.getBody().user();
    }

    record BandercraftUser(long id, String username, String real_name) {}
    record Wrap(BandercraftUser user) {}
}
