package bts.delation.controller;

import bts.delation.model.CustomOAuth2User;
import bts.delation.model.Feedback;
import bts.delation.model.dto.FeedbackPage;
import bts.delation.model.enums.FeedbackType;
import bts.delation.model.enums.Status;
import bts.delation.model.dto.FeedbackDTO;
import bts.delation.model.dto.HistoryRecordDTO;
import bts.delation.model.dto.UserDTO;
import bts.delation.repo.FeedbackSearchService;
import bts.delation.repo.dto.FeedbackSearchQuery;
import bts.delation.service.FeedbackFlowService;
import bts.delation.service.FeedbackService;
import bts.delation.service.HistoryService;
import bts.delation.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.ZoneOffset;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Controller
@RequestMapping("/moder/feedback")
@RequiredArgsConstructor
public class FeedbackController {

    private final FeedbackService feedbackService;
    private final FeedbackFlowService feedbackFlowService;
    private final UserService userService;
    private final HistoryService historyService;

    @GetMapping
    public String page(
            @AuthenticationPrincipal CustomOAuth2User user,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String status,
            Model model
    ) {
        FeedbackType feedbackType = parseType(type);
        Status status1 = parseStatus(status);
        Integer parsedPage = page == null ? 0 : page;

        Predicate<Feedback> feedbackPredicate = switch (user.getRole()) {
            case CLIENT -> f -> false;
            case MODER -> f -> !f.getType().equals(FeedbackType.APPEAL_MODER);
            case ADMIN -> f -> true;
        };

        FeedbackPage result = feedbackService.getAll(new FeedbackSearchQuery(
                feedbackType == null ? Collections.emptyList() : Collections.singletonList(feedbackType),
                status1 == null ? Collections.emptyList() : Collections.singletonList(status1),
                parsedPage,
                10
        ), feedbackPredicate);

        List<FeedbackDTO> all = result
                .feedbacks()
                .stream()
                .sorted(Comparator.comparing(Feedback::getCreatedAt).reversed())
                .sorted(Comparator.comparing(feedback -> feedback.getStatus().priority()))
                .map(FeedbackController::mapToDto)
                .toList();

        List<UserDTO> moders = userService.findModers()
                .stream()
                .map(u -> new UserDTO(u.getId(), u.getEmail()))
                .toList();

        model.addAttribute("list", all)
                .addAttribute("moders", moders)
                .addAttribute("types", FeedbackType.values())
                .addAttribute("statuses", Status.values())
                .addAttribute("currentPage", result.page())
                .addAttribute("currentPageSize", result.size())
                .addAttribute("currentTotal", result.total())
                .addAttribute("listPageNumbers", IntStream.range(0, ((int) (result.total() / result.size())) + 1).toArray());

        addCurrentFilterPosition(model, feedbackType, status1);

        return "feedbacks";
    }

    @GetMapping("/{id}")
    public String page(@PathVariable Long id, Model model) {

        Feedback feedback = feedbackService.getById(id);
        FeedbackDTO feedbackDTO = mapToDto(feedback);

        List<UserDTO> moders = userService.findModers()
                .stream()
                .map(u -> new UserDTO(u.getId(), u.getEmail()))
                .toList();

        List<HistoryRecordDTO> historyRecords = historyService.feedbackHistory(feedback)
                .stream()
                .map(hr -> new HistoryRecordDTO(
                        hr.getId(),
                        hr.getType().name(),
                        hr.getAuthor(),
                        hr.getBefore(),
                        hr.getAfter(),
                        hr.getComment(),
                        Date.from(hr.getTime().toInstant(ZoneOffset.UTC))
                ))
                .sorted(Comparator.comparing(HistoryRecordDTO::time).reversed())
                .collect(Collectors.toList());

        model.addAttribute("feedback", feedbackDTO);
        model.addAttribute("moders", moders);
        model.addAttribute("status", Status.values());
        model.addAttribute("history", historyRecords);

        return "feedback";
    }

    @PostMapping("/add-comment")
    public String addComment(@RequestParam Long id,
                             @RequestParam String comment,
                             @AuthenticationPrincipal CustomOAuth2User user,
                             Model model) {

        if (!user.isAdmin() && !user.isModer()) {
            return "redirect:/404";
        }

        feedbackService.addComment(id, comment, user.getEmail());

        return "redirect:/moder/feedback/" + id;
    }

    @PostMapping("/move")
    public String moveTo(@RequestParam Long id,
                         @RequestParam String status,
                         @AuthenticationPrincipal CustomOAuth2User user,
                         Model model) {

        if (!user.isAdmin() && !user.isModer()) {
            return "redirect:/404";
        }

        feedbackFlowService.manageStatusFlow(id, Status.valueOf(status), user.getEmail());

        return "redirect:/moder/feedback/" + id;
    }

    @PostMapping("/assign")
    public String assign(
            @RequestParam String moder,
            @RequestParam Long id,
            @RequestParam String from,
            @AuthenticationPrincipal CustomOAuth2User user,
            Model model
    ) {
        feedbackService.assignModer(id, moder, user.getEmail());

        if (from.equals("list")) {
            return "redirect:/moder/feedback";
        } else if (from.equals("single")) {
            return "redirect:/moder/feedback/" + id;
        }
        return "redirect:/moder/feedback";
    }

    private void addCurrentFilterPosition(Model model, FeedbackType type, Status status) {

        if (type != null) model.addAttribute("filterCurrentType", type);
        if (status != null) model.addAttribute("filterCurrentStatus", status);
    }

    private static Status parseStatus(String s) {
        Status status = null;
        if (Objects.nonNull(s) && !s.equals("")) {
            status = Status.valueOf(s);
        }
        return status;
    }

    private static FeedbackType parseType(String type) {
        FeedbackType feedbackType = null;
        if (Objects.nonNull(type) && !type.equals("")) {
            feedbackType = FeedbackType.valueOf(type);
        }
        return feedbackType;
    }

    public static FeedbackDTO mapToDto(Feedback feedback) {
        return new FeedbackDTO(
                feedback.getId(),
                feedback.getAuthor().getDiscordUsername(),
                feedback.getModer() == null ? "empty" : feedback.getModer().getUsername(),
                feedback.getMentions(),
                feedback.getText(),
                feedback.getStatus().name(),
                feedback.getType(),
                feedback.getAttachmentUrl(),
                feedback.getReviewComment(),
                Date.from(feedback.getCreatedAt().toInstant(ZoneOffset.UTC))
        );
    }
}
