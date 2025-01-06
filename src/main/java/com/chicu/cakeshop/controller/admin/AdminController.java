package com.chicu.cakeshop.controller.admin;


import com.chicu.cakeshop.enums.SettingEnum;
import com.chicu.cakeshop.model.User;
import com.chicu.cakeshop.service.ImageService;
import com.chicu.cakeshop.service.SettingService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final SettingService settingService;

    private final ImageService imageService;

    public AdminController(SettingService settingService,
                           ImageService imageService) {
        this.settingService = settingService;
        this.imageService = imageService;
    }

    @GetMapping("")
    public String index(Model model, @AuthenticationPrincipal User user) {
        model.addAttribute("user", user);
        return "admin/index";
    }


}
