package com.chicu.cakeshop.service.impl;


import com.chicu.cakeshop.enums.MailType;
import com.chicu.cakeshop.model.User;
import com.chicu.cakeshop.service.MailService;
import jakarta.mail.internet.MimeMessage;
import lombok.SneakyThrows;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;

import static com.chicu.cakeshop.enums.MailType.NOTIFICATION;
import static com.chicu.cakeshop.enums.MailType.REGISTRATION;

@Service
public class MailServiceImpl implements MailService {
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    public MailServiceImpl(JavaMailSender mailSender, TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    @Override
    public void sendMail(User user, MailType mailType) {
        switch (mailType) {
            case REGISTRATION -> sendRegistrationMail(user);
            case NOTIFICATION -> sendNotificationMail(user);
            default -> {}
        }
    }
    @SneakyThrows
    private void sendNotificationMail(User user) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,true,"UTF-8");
        helper.setTo(user.getEmail());
        helper.setSubject("Спасибо за ваш заказ " + user.getFirstname());
        String emailContent = getEmailContentOrder(user);
        helper.setText(emailContent,true);
        mailSender.send(mimeMessage);
    }


    @SneakyThrows
    private void sendRegistrationMail(User user) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,false,"UTF-8");
        helper.setTo(user.getEmail());
        String emailContent = getEmailContentRegistry(user);
        helper.setText(emailContent,true);
        mailSender.send(mimeMessage);
    }
    private String getEmailContentRegistry(User user) {
        return "<html>" +
                "<body>" +
                "<h1>Добро пожаловать, " + user.getFirstname() + "!</h1>" +
                "<p>Спасибо за регистрацию в TORTIX. Мы рады видеть вас среди наших пользователей.</p>" +
                "<p>Ваш логин: " + user.getEmail() + "</p>" +
                "<p>Если у вас есть какие-либо вопросы, не стесняйтесь обращаться к нашей поддержке.</p>" +
                "<p>С уважением,</p>" +
                "<p>Команда TORTIX</p>" +
                "</body>" +
                "</html>";
    }
    private String getEmailContentOrder(User user) {
        return "<html>" +
                "<body>" +
                "<h1>Спасибо за ваш заказ, " + user.getFirstname() + "!</h1>" +
                "<p>Детали заказа:</p>" +
                "<p>" + user.getOrderDetails() + "</p>" +
                "<p>С уважением,</p>" +
                "<p>Команда TORTIX</p>" +
                "</body>" +
                "</html>";
    }
}
