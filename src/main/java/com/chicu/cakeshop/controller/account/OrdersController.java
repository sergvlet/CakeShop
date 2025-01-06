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
public class OrdersController {
    private final LocaleResolver localeResolver;

    public OrdersController(LocaleResolver localeResolver) {
        this.localeResolver = localeResolver;
    }

    @GetMapping(path =  "/order")
    public String order(@AuthenticationPrincipal User user, @RequestParam(value = "lang", required = false) String lang,
                       HttpServletRequest request, HttpServletResponse response, Model model) {

        if ((!"ru".equals(lang) && !"ro".equals(lang))) {
            lang = "ru";
            Locale newLocale = new Locale(lang);
            localeResolver.setLocale(request, response, newLocale);
            Cookie cookie = new Cookie("lang", lang);
            cookie.setMaxAge(30 * 24 * 60 * 60);
            response.addCookie(cookie);
        }

        model.addAttribute("user", user);
        model.addAttribute("lang", lang);

        return "account/orders";
    }


}
