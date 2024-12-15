package com.example.demo.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendSimpleEmail(String toEmail, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(body);
        // message.setFrom("your-email@gmail.com");  // Optional, sets the sender email

        mailSender.send(message);
    }

    public void sendSimpleEmailContact(String toEmail, String subject, String body, String fromEmail, String userId) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(body + "\n\n\nEmail was send by: " + fromEmail + "\nUser account ID: " + userId);

        mailSender.send(message);
    }
}
