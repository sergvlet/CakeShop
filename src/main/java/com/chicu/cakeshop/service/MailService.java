package com.chicu.cakeshop.service;


import com.chicu.cakeshop.enums.MailType;
import com.chicu.cakeshop.model.User;
import org.springframework.stereotype.Service;

@Service
public interface MailService {
     void sendMail(User user, MailType mailType);
}
