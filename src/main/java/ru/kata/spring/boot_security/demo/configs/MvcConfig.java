package ru.kata.spring.boot_security.demo.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    public void addViewControllers(ViewControllerRegistry registry) {
        // Для страницы входа (логин)
        registry.addViewController("/login").setViewName("login");

        // Для главной страницы (можно перенаправить на логин или на /people)
        registry.addViewController("/").setViewName("index");

        // У тебя уже есть:
        registry.addViewController("/user").setViewName("user");

        // Можно добавить для ошибок
        registry.addViewController("/access-denied").setViewName("access-denied");
    }
}