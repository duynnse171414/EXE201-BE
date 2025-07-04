package com.example.swd391_be_hiv.service;

import com.example.swd391_be_hiv.entity.Reminder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendReminderEmail(Reminder reminder) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(reminder.getCustomer().getAccount().getEmail());
            message.setSubject("Nhắc nhở: " + reminder.getReminderType());
            message.setText(buildEmailContent(reminder));
            message.setFrom("your-email@gmail.com");

            mailSender.send(message);
            System.out.println("Email sent successfully to: " + reminder.getCustomer().getAccount().getEmail());
        } catch (Exception e) {
            System.err.println("Error sending email: " + e.getMessage());
            throw new RuntimeException("Failed to send email", e);
        }
    }

    private String buildEmailContent(Reminder reminder) {
        return String.format("""
            Xin chào %s,
            
            Đây là lời nhắc nhở về: %s
            
            Nội dung: %s
            
            Ngày nhắc nhở: %s
            
            Trân trọng,
            Hệ thống HIV Management
            """,
                reminder.getCustomer().getAccount().getFullName(),
                reminder.getReminderType(),
                reminder.getMessage(),
                reminder.getReminderDate()
        );
    }
}