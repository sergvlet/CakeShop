package com.chicu.cakeshop.controller.account;

import com.chicu.cakeshop.model.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/account")
public class SettingsController {
    @GetMapping(path =  "/settings")
    public String settings(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("user", user);
        return "account/settings";
    }
}
