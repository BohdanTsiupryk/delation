package bts.delation.controller;

import bts.delation.model.Feedback;
import bts.delation.model.dto.FeedbackDTO;
import bts.delation.model.dto.UserDTO;
import bts.delation.service.FeedbackService;
import bts.delation.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/moder/feedback")
@RequiredArgsConstructor
public class FeedbackController {

    private final FeedbackService feedbackService;
    private final UserService userService;


    @GetMapping
    public String page(Model model) {
        List<FeedbackDTO> all = feedbackService.getAll()
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

        FeedbackDTO feedback = mapToDto(feedbackService.getOne(id));

        List<UserDTO> moders = userService.findModers()
                .stream()
                .map(u -> new UserDTO(u.getId(), u.getEmail()))
                .toList();


        model.addAttribute("feedback", feedback);
        model.addAttribute("moders", moders);

        return "feedback";
    }

    @PostMapping("/assign")
    public String assign(
            @RequestParam String moder,
            @RequestParam String id,
            Model model
    ) {
        feedbackService.assignModer(id, moder);

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
                Date.from(feedback.getCreatedAt().toInstant(ZoneOffset.UTC))
        );
    }
}
