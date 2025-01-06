package com.chicu.cakeshop.controller;


import com.chicu.cakeshop.exception.PancakeException;
import com.chicu.cakeshop.exception.UserAlreadyExistException;
import com.chicu.cakeshop.model.User;
import com.chicu.cakeshop.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class LoginController {


    private UserService userService;

    public LoginController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/login")
    public String login(Model model,
                        @RequestParam(required = false) Boolean fail,
                        @RequestParam(value = "lang", required = false) String lang) {
        model.addAttribute("fail", fail != null && fail);
        model.addAttribute("lang", lang);
        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        request.getSession().invalidate();
        return "redirect:/login";
    }

    @GetMapping("/register")
    public String register(Model model, @RequestParam(required = false) String error) {
        model.addAttribute("user", new User());  // Новый объект для формы регистрации
        model.addAttribute("error", error);      // Передача ошибки, если она есть
        return "signup";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute User user, Model model) {
        try {
            userService.updateUser(user);  // Сохранение нового пользователя
        } catch (UserAlreadyExistException ex) {
            model.addAttribute("error", "emailIsBusy"); // Ошибка: email занят
            model.addAttribute("user", user);
            return "signup";
        } catch (PancakeException e) {
            model.addAttribute("error", "wrongEmailField"); // Ошибка: неверный формат email
            model.addAttribute("user", user);
            return "signup";
        }
        return "redirect:/login";  // После успешной регистрации переходим на страницу логина
    }

    @PostMapping("/userExist")
    @ResponseBody
    public Boolean userExist(@RequestParam String email) {
        return userService.userExistsByEmail(email);  // Проверка, занят ли email
    }
    @GetMapping("/forgot-password")
    public String forgotpassword(Model model,
                        @RequestParam(required = false) Boolean fail,
                        @RequestParam(value = "lang", required = false) String lang) {
        model.addAttribute("fail", fail != null && fail);
        model.addAttribute("lang", lang);
        return "forgot-password";
    }

}
