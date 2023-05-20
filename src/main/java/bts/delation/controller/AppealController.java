package bts.delation.controller;

import bts.delation.model.Appeal;
import bts.delation.service.AppealService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/moder/appeal")
@RequiredArgsConstructor
public class AppealController {

    private final AppealService service;


    @GetMapping
    public String page(Model model) {
        List<Appeal> all = service.getAll();

        model.addAttribute("list", all);

        return "appeal";
    }
}
