package bts.delation.controller;

import bts.delation.model.CustomOAuth2User;
import bts.delation.model.Feedback;
import bts.delation.model.enums.Status;
import bts.delation.model.dto.FeedbackDTO;
import bts.delation.model.dto.HistoryRecordDTO;
import bts.delation.model.dto.UserDTO;
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
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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
            Model model
    ) {
        List<FeedbackDTO> all = feedbackService.getAll(user.getRole())
                .stream()
                .map(this::mapToDto)
                .toList();
        List<UserDTO> moders = userService.findModers()
                .stream()
                .map(u -> new UserDTO(u.getId(), u.getEmail()))
                .toList();

        model.addAttribute("list", all);
        model.addAttribute("moders", moders);

        return "feedbacks";
    }

    @GetMapping("/{id}")
    public String page(@PathVariable String id, Model model) {

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
                .sorted(Comparator.comparing(HistoryRecordDTO::time))
                .collect(Collectors.toList());

        model.addAttribute("feedback", feedbackDTO);
        model.addAttribute("moders", moders);
        model.addAttribute("status", Status.values());
        model.addAttribute("history", historyRecords);

        return "feedback";
    }

    @PostMapping("/add-comment")
    public String addComment(@RequestParam String id,
                             @RequestParam String comment,
                             @AuthenticationPrincipal CustomOAuth2User user,
                             Model model) {

        if (!user.isAdmin() && !user.isModer()) {
            return "redirect:/index";
        }

        feedbackService.addComment(id, comment, (String) user.getClaims().get("email"));

        return "redirect:/moder/feedback";
    }

    @PostMapping("/move")
    public String moveTo(@RequestParam String id,
                         @RequestParam String status,
                         @AuthenticationPrincipal CustomOAuth2User user,
                         Model model) {

        if (!user.isAdmin() && !user.isModer()) {
            return "redirect:/index";
        }

        feedbackFlowService.manageStatusFlow(id, Status.valueOf(status), (String) user.getClaims().get("email"));

        return "redirect:/moder/feedback";
    }

    @PostMapping("/assign")
    public String assign(
            @RequestParam String moder,
            @RequestParam String id,
            @AuthenticationPrincipal CustomOAuth2User user,
            Model model
    ) {
        feedbackService.assignModer(id, moder, (String) user.getClaims().get("email"));

        return "redirect:/moder/feedback";
    }


    public FeedbackDTO mapToDto(Feedback feedback) {
        return new FeedbackDTO(
                feedback.getId(),
                feedback.getAuthor().getDiscordUsername(),
                feedback.getModer() == null ? "empty" : feedback.getModer().getUsername(),
                feedback.getMentions(),
                feedback.getText(),
                feedback.getStatus().name(),
                feedback.getType().getUa(),
                feedback.getAttachmentUrl(),
                feedback.getReviewComment(),
                Date.from(feedback.getCreatedAt().toInstant(ZoneOffset.UTC))
        );
    }
}
