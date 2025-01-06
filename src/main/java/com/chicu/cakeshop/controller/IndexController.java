package com.chicu.cakeshop.controller;


import com.chicu.cakeshop.model.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(method = {RequestMethod.GET})
public class IndexController {
    @GetMapping(path = {"", "/", "/index"})
    public String home(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("user", user);
        return "index";
    }
}
