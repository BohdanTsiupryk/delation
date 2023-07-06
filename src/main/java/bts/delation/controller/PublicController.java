package bts.delation.controller;

import bts.delation.service.FeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

@Controller
@RequestMapping("/public")
@RequiredArgsConstructor
public class PublicController {

    private final FeedbackService feedbackService;

    @GetMapping("/feedback/{id}")
    public String feedbackPublicView(@PathVariable Long id, Model model) {

        PublicFeedbackDto publicFeedbackDto = Optional.ofNullable(feedbackService.getById(id))
                .map(f -> new PublicFeedbackDto(
                        f.getStatus().name(),
                        Date.from(f.getCreatedAt().toInstant(ZoneOffset.UTC)),
                        Objects.nonNull(f.getModer()) ? f.getModer().getUsername() : "",
                        f.getText(),
                        f.getType().getUa(),
                        f.getAuthor().getDiscordUsername(),
                        f.getReviewComment(),
                        f.getAttachmentUrl()
                ))
                .get();

        model.addAttribute("feedback",publicFeedbackDto);

        return "public-feedback";
    }

    public record PublicFeedbackDto(
            String status,
            Date date,
            String moder,
            String text,
            String type,
            String author,
            String comment,
            String attUrl
    ) {}
}
