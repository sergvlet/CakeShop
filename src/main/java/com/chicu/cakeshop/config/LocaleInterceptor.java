package com.chicu.cakeshop.config;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.LocaleResolver;

import java.util.Locale;

@Component
public class LocaleInterceptor implements HandlerInterceptor {

    private final LocaleResolver localeResolver;

    public LocaleInterceptor(LocaleResolver localeResolver) {
        this.localeResolver = localeResolver;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String lang = request.getParameter("lang");
        if (lang != null && (lang.equals("ru") || lang.equals("ro"))) {
            Locale newLocale = new Locale(lang);
            localeResolver.setLocale(request, response, newLocale);

            Cookie cookie = new Cookie("lang", lang);
            cookie.setMaxAge(30 * 24 * 60 * 60); // 30 дней
            response.addCookie(cookie);
        }
        return true;
    }
}
