package com.chicu.cakeshop;

import jakarta.servlet.DispatcherType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration {

    private final UserDetailsService userDetailsService;

    public SecurityConfiguration(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable) // Отключаем CSRF для упрощения, если это безопасно для вашего приложения
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(getPublicEndpoints()).permitAll() // Разделяем публичные эндпоинты
                        .requestMatchers(getAdminEndpoints()).hasRole("ADMIN") // Административные эндпоинты
                        .requestMatchers(getAccountEndpoints()).hasRole("USER") // Административные эндпоинты
                        .dispatcherTypeMatchers(DispatcherType.FORWARD, DispatcherType.ERROR).permitAll()
                        .anyRequest().authenticated() //  остальные запросы требуют аутентификации
                )
                .formLogin(formLogin -> formLogin
                        .loginPage("/login")
                        .usernameParameter("email")
                        .passwordParameter("password")
                        .defaultSuccessUrl("/index", true)
                        .failureUrl("/login?fail=true")
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/index")
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(daoAuthenticationProvider);
    }

    /**
     * Эндпоинты, доступные без аутентификации.
     */
    private String[] getPublicEndpoints() {
        return new String[]{
                "/", "/index", "/login", "/register", "/userExist", "/changeLanguage/**",
                "/images", "/forgot-password","/shop", "/order/**", "/blog", "/blog-detail/**", "/contact",
                "/assets/**", "/js/**", "/css/**"
        };
    }

    /**
     * Эндпоинты, доступные только для администраторов.
     */
    private String[] getAdminEndpoints() {
        return new String[]{
                "/admin", "/admin/**"
        };
    }

    /**
     * Эндпоинты, доступные только для зарегестрируемых пользователей.
     */
    private String[] getAccountEndpoints() {
        return new String[]{"/account","/account/**"};
    }
}
