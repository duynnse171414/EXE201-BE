package com.example.swd391_be_hiv.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTasks {

    @Autowired
    private ReminderService reminderService;

    // Chạy mỗi ngày lúc 9:00 AM
    @Scheduled(cron = "0 0 8 * * *")
    public void sendDailyReminders() {
        System.out.println("Running daily reminder task...");
        reminderService.sendAllPendingReminders();
    }
}