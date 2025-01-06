package com.chicu.cakeshop.controller.account;

import com.chicu.cakeshop.model.User;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.LocaleResolver;

import java.util.Locale;

@Controller
@RequestMapping("/account")
public class NotificationController {
    @GetMapping(path = "/notification")
    public String home(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("user", user);
        return "account/notification";
    }
}
