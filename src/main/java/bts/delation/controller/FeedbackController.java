package bts.delation.controller;

import bts.delation.model.Feedback;
import bts.delation.service.FeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/moder/appeal")
@RequiredArgsConstructor
public class FeedbackController {

    private final FeedbackService service;


    @GetMapping
    public String page(Model model) {
        List<Feedback> all = service.getAll();

        model.addAttribute("list", all);

        return "feedback";
    }
}
