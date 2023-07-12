package bts.delation.controller;

import bts.delation.exception.NotFoundException;
import bts.delation.model.CustomOAuth2User;
import bts.delation.model.Feedback;
import bts.delation.model.dto.FeedbackDTO;
import bts.delation.model.dto.FeedbackPage;
import bts.delation.model.enums.FeedbackType;
import bts.delation.model.enums.Status;
import bts.delation.repo.dto.FeedbackSearchQuery;
import bts.delation.service.FeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/public")
@RequiredArgsConstructor
public class PublicController {

    private final FeedbackService feedbackService;

    @GetMapping("/feedback/{id}")
    public String feedbackPublicView(@PathVariable Long id, Model model) {

        Optional<PublicFeedbackDto> optionalPublicFeedbackDto = Optional.ofNullable(feedbackService.getById(id))
                .map(f -> new PublicFeedbackDto(
                        f.getId().toString(),
                        f.getStatus().name(),
                        Date.from(f.getCreatedAt().toInstant(ZoneOffset.UTC)),
                        Objects.nonNull(f.getModer()) ? f.getModer().getUsername() : "",
                        f.getText(),
                        f.getType().getUa(),
                        f.getAuthor().getDiscordUsername(),
                        f.getReviewComment(),
                        f.getAttachmentUrl()
                ));

        if (optionalPublicFeedbackDto.isEmpty()) return "redirect:/404";

        model.addAttribute("feedback", optionalPublicFeedbackDto.get());

        return "public-feedback";
    }

    @GetMapping("/feedbacks")
    public String clientPage(
            @RequestParam(required = false) Integer page,
            Model model
    ) {
        Integer parsedPage = page == null ? 0 : page;

        FeedbackPage result = feedbackService.getAll(new FeedbackSearchQuery(
                Collections.singletonList(FeedbackType.FEEDBACK),
                Collections.singletonList(Status.DONE),
                parsedPage,
                10
        ), feedback -> true);

        List<FeedbackDTO> all = result
                .feedbacks()
                .stream()
                .sorted(Comparator.comparing(Feedback::getCreatedAt).reversed())
                .sorted(Comparator.comparing(feedback -> feedback.getStatus().priority()))
                .map(FeedbackController::mapToDto)
                .toList();

        model.addAttribute("feedbacks", all)
                .addAttribute("currentPage", result.page())
                .addAttribute("currentPageSize", result.size())
                .addAttribute("currentTotal", result.total())
                .addAttribute("listPageNumbers", IntStream.range(0, ((int) (result.total() / result.size())) + 1).toArray());

        return "client-feedbacks";
    }

    @ExceptionHandler(NotFoundException.class)
    public String handleNotFound(NotFoundException e, Model model) {

        return "redirect:/error/404";
    }

    public record PublicFeedbackDto(
            String id,
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
