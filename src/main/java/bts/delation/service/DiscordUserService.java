package bts.delation.service;

import bts.delation.exception.NotFoundException;
import bts.delation.model.DiscordUser;
import bts.delation.repo.DiscordUserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class DiscordUserService {

    private final DiscordUserRepo discordUserRepo;
    private final RestTemplate restTemplate;

    public List<DiscordUser> findAllUsers() {
        return discordUserRepo.findAll();
    }

    public void saveAndSync(String id, String name) {
        DiscordUser save = discordUserRepo.save(new DiscordUser(id, name));

        try {
            syncUserWithMine(save.getId(), save.getDiscordUsername());
        } catch (Exception e) {}
    }

    public DiscordUser getById(String id) {
        return discordUserRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Discord user not found"));
    }

    public boolean checkUserAutorize(String id, String name) {

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

    public Optional<DiscordUser> getNotSyncedUser() {
        return discordUserRepo.findNotSynced()
                .stream().findFirst();
    }

    @Async
    public void saveBulk(List<DiscordUser> users) {
        List<List<DiscordUser>> lists = splitListIntoChunks(users, 20);

        lists.forEach(discordUserRepo::saveAll);
    }

    private static <T> List<List<T>> splitListIntoChunks(List<T> list, int chunkSize) {
        return IntStream.range(0, (list.size() + chunkSize - 1) / chunkSize)
                .mapToObj(i -> list.subList(i * chunkSize, Math.min((i + 1) * chunkSize, list.size())))
                .collect(Collectors.toList());
    }

    private DiscordUser save(DiscordUser discordUser) {

        return discordUserRepo.save(discordUser);
    }

    private boolean syncUserWithMine(String id, String name) {

        BandercraftUser userFromMine;
        try {
            userFromMine = getUserFromMine(id);
        } catch (NotFoundException e) {

            save(new DiscordUser(id, name));

            return false;
        }

        save(new DiscordUser(
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

    record BandercraftUser(long id, String username, String real_name) {
    }

    record Wrap(BandercraftUser user) {
    }
}
