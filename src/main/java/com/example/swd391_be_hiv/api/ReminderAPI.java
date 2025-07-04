package com.example.swd391_be_hiv.api;

import com.example.swd391_be_hiv.entity.Reminder;
import com.example.swd391_be_hiv.model.reponse.ReminderResponse;
import com.example.swd391_be_hiv.model.request.ReminderRequest;
import com.example.swd391_be_hiv.service.ReminderService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@SecurityRequirement(name = "api")
@RestController
@RequestMapping("/api/reminders")
public class ReminderAPI {

    @Autowired
    private ReminderService reminderService;

    @PostMapping("/{reminderId}/send-email")
    public ResponseEntity<String> sendReminderEmail(@PathVariable Long reminderId) {
        try {
            reminderService.sendReminderEmail(reminderId);
            return ResponseEntity.ok("Email sent successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to send email: " + e.getMessage());
        }
    }

    @GetMapping("/pending")
    public ResponseEntity<List<Reminder>> getPendingReminders() {
        List<Reminder> pendingReminders = reminderService.getPendingReminders();
        return ResponseEntity.ok(pendingReminders);
    }

    @PostMapping("/send-all-pending")
    public ResponseEntity<String> sendAllPendingReminders() {
        try {
            reminderService.sendAllPendingReminders();
            return ResponseEntity.ok("All pending reminders sent successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to send reminders: " + e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<ReminderResponse> createReminder(@Valid @RequestBody ReminderRequest requestDTO) {
        ReminderResponse response = reminderService.createReminder(requestDTO);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/getReminders")
    public ResponseEntity<List<ReminderResponse>> getAllReminders() {
        List<ReminderResponse> reminders = reminderService.getAllReminders();
        return ResponseEntity.ok(reminders);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReminderResponse> getReminderById(@PathVariable Long id) {
        ReminderResponse response = reminderService.getReminderById(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReminderResponse> updateReminder(@Valid @RequestBody ReminderRequest requestDTO, @PathVariable Long id) {
        ReminderResponse response = reminderService.updateReminder(requestDTO, id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ReminderResponse> deleteReminder(@PathVariable Long id) {
        ReminderResponse response = reminderService.deleteReminder(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<ReminderResponse>> getRemindersByCustomer(@PathVariable Long customerId) {
        List<ReminderResponse> reminders = reminderService.getRemindersByCustomer(customerId);
        return ResponseEntity.ok(reminders);
    }

    @GetMapping("/type/{reminderType}")
    public ResponseEntity<List<ReminderResponse>> getRemindersByType(@PathVariable String reminderType) {
        List<ReminderResponse> reminders = reminderService.getRemindersByType(reminderType);
        return ResponseEntity.ok(reminders);
    }
}