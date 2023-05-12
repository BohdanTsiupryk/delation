package bts.delation.controller;

import bts.delation.model.CustomOAuth2User;
import bts.delation.model.User;
import bts.delation.model.UserRole;
import bts.delation.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;

    @GetMapping
    public String adminPage(Model model) {

        List<User> users = userService.getUsers();

        model.addAttribute("users", users);
        model.addAttribute("roles", UserRole.values());
        return "admin-page";
    }

    @PostMapping("/change-role")
    public String changeRole(
            @RequestParam String activeRole,
            @RequestParam String id,
            @AuthenticationPrincipal CustomOAuth2User principal
            ) {

        User user = userService.getById(id);
        if (principal.getAuthorities().contains(UserRole.ADMIN)) {
            user.setUserRole(UserRole.valueOf(activeRole));
            userService.save(user);
        }

        return "redirect:/admin";
    }
}
