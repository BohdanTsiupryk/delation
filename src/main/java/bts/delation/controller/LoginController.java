package bts.delation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class LoginController {

    @GetMapping("/logout")
    public String logout() {
        SecurityContextHolder.getContext().getAuthentication().setAuthenticated(false);
        return "index";
    }
}
