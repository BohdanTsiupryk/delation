package bts.delation.controller;

import bts.delation.model.*;
import bts.delation.model.dto.ProfileUserDto;
import bts.delation.model.enums.FeedbackType;
import bts.delation.model.enums.SyncTarget;
import bts.delation.service.FeedbackService;
import bts.delation.service.SyncService;
import bts.delation.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final UserService userService;
    private final SyncService syncService;
    private final FeedbackService feedbackService;

    @GetMapping
    public String profile(
            @AuthenticationPrincipal CustomOAuth2User user,
            @RequestParam(required = false) String id,
            Model model
    ) {
        User byId = id == null
                ? userService.getById(user.getId())
                : userService.getById(id);
        ProfileUserDto profileUserDto = Optional.of(byId)
                .map(u -> {
                    Optional<DiscordUser> discordUser = Optional.ofNullable(u.getDiscordUser());

                    discordUser.ifPresent(d -> addDelations(model, d));

                    return new ProfileUserDto(
                            u.getId(),
                            u.getEmail(),
                            u.getUserRole().name(),
                            discordUser.isPresent(),
                            discordUser.map(DiscordUser::getId).orElse(""),
                            discordUser.map(DiscordUser::getDiscordUsername).orElse(""),
                            discordUser.map(DiscordUser::getMineUsername).orElse(""),
                            discordUser.map(DiscordUser::isSyncWithMine).orElse(false)
                    );
                }).get();

        syncService.getDiscordCode(byId)
                .ifPresent(c -> model.addAttribute("discordCode", c.getCode()));


        model.addAttribute("prof", profileUserDto);
        return "profile";
    }

    @GetMapping("/createDiscordCode")
    public String syncDiscord(@AuthenticationPrincipal CustomOAuth2User user,
                              Model model) {

        User byId = userService.getById(user.getId());
        if (Objects.isNull(byId.getDiscordUser())) {
            syncService.createCode(byId, SyncTarget.DISCORD);
        }

        return "redirect:/profile";
    }

    private void addDelations(Model model, DiscordUser byId) {
        List<FeedbackProfileDto> delations = feedbackService.getByAuthor(byId.getId())
                .stream()
                .filter(f -> List.of(FeedbackType.APPEAL_MODER, FeedbackType.APPEAL, FeedbackType.BUG).contains(f.getType()))
                .sorted(Comparator.comparing(f -> f.getStatus().priority()))
                .map(f -> new FeedbackProfileDto(f.getId(), f.getType().getUa(), f.getStatus().name()))
                .collect(Collectors.toList());
        model.addAttribute("delation", delations);
    }

    public record FeedbackProfileDto(
            String id,
            String type,
            String status
    ) {}
}
