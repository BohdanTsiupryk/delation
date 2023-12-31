package bts.delation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping
@RequiredArgsConstructor
public class IndexController {


    @GetMapping("/index")
    public String sss() {
        return "index";
    }


    @GetMapping("/error/404")
    public String notFound(Model model) {
        return "404";
    }
}
