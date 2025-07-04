package com.example.swd391_be_hiv.service;

import com.example.swd391_be_hiv.entity.Customer;
import com.example.swd391_be_hiv.entity.Reminder;
import com.example.swd391_be_hiv.exception.DuplicateEntity;
import com.example.swd391_be_hiv.exception.NotFoundException;
import com.example.swd391_be_hiv.model.reponse.ReminderResponse;
import com.example.swd391_be_hiv.model.request.ReminderRequest;
import com.example.swd391_be_hiv.repository.CustomerRepository;
import com.example.swd391_be_hiv.repository.ReminderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReminderService {

    @Autowired
    private ReminderRepository reminderRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    private EmailService emailService;

    @Transactional
    public void sendReminderEmail(Long reminderId) {
        Reminder reminder = reminderRepository.findById(reminderId)
                .orElseThrow(() -> new RuntimeException("Reminder not found"));

        if (reminder.getIsSent()) {
            throw new RuntimeException("Email already sent for this reminder");
        }

        emailService.sendReminderEmail(reminder);

        // Cập nhật trạng thái đã gửi
        reminder.setIsSent(true);
        reminderRepository.save(reminder);
    }

    public List<Reminder> getPendingReminders() {
        return reminderRepository.findByIsSentFalseAndReminderDateLessThanEqual(LocalDate.now());
    }

    @Transactional
    public void sendAllPendingReminders() {
        List<Reminder> pendingReminders = getPendingReminders();

        for (Reminder reminder : pendingReminders) {
            try {
                emailService.sendReminderEmail(reminder);
                reminder.setIsSent(true);
                reminderRepository.save(reminder);
            } catch (Exception e) {
                System.err.println("Failed to send reminder email for ID: " + reminder.getReminderId());
            }
        }
    }
    public ReminderResponse createReminder(ReminderRequest requestDTO) {
        try {
            Customer customer = customerRepository.findById(requestDTO.getCustomerId())
                    .orElseThrow(() -> new NotFoundException("Customer not found"));

            Reminder reminder = new Reminder();
            reminder.setCustomer(customer);
            reminder.setReminderType(requestDTO.getReminderType());
            reminder.setMessage(requestDTO.getMessage());
            reminder.setReminderDate(requestDTO.getReminderDate());
            reminder.setIsSent(false); // Default to false

            Reminder savedReminder = reminderRepository.save(reminder);
            return convertToResponseDTO(savedReminder);

        } catch (Exception e) {
            throw new DuplicateEntity("Error creating reminder: " + e.getMessage());
        }
    }

    public List<ReminderResponse> getAllReminders() {
        return reminderRepository.findAll()
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }
    public List<ReminderResponse> getRemindersByType(String reminderType) {
        return reminderRepository.findRemindersByReminderType(reminderType)
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }
    public List<ReminderResponse> getRemindersByCustomer(Long customerId) {
        return reminderRepository.findRemindersByCustomerId(customerId)
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }
    public ReminderResponse getReminderById(Long reminderId) {
        Reminder reminder = reminderRepository.findReminderByReminderId(reminderId);
        if (reminder == null) {
            throw new NotFoundException("Reminder not found");
        }
        return convertToResponseDTO(reminder);
    }

    public ReminderResponse updateReminder(ReminderRequest requestDTO, Long reminderId) {
        Reminder existingReminder = reminderRepository.findReminderByReminderId(reminderId);
        if (existingReminder == null) {
            throw new NotFoundException("Reminder not found");
        }

        Customer customer = customerRepository.findById(requestDTO.getCustomerId())
                .orElseThrow(() -> new NotFoundException("Customer not found"));

        existingReminder.setCustomer(customer);
        existingReminder.setReminderType(requestDTO.getReminderType());
        existingReminder.setMessage(requestDTO.getMessage());
        existingReminder.setReminderDate(requestDTO.getReminderDate());

        Reminder updatedReminder = reminderRepository.save(existingReminder);
        return convertToResponseDTO(updatedReminder);
    }

    public ReminderResponse deleteReminder(Long reminderId) {
        Reminder reminder = reminderRepository.findReminderByReminderId(reminderId);
        if (reminder == null) {
            throw new NotFoundException("Reminder not found");
        }

        ReminderResponse response = convertToResponseDTO(reminder);
        reminderRepository.delete(reminder);
        return response;
    }
    private ReminderResponse convertToResponseDTO(Reminder reminder) {
        ReminderResponse dto = new ReminderResponse();
        dto.setReminderId(reminder.getReminderId());
        dto.setCustomerId(reminder.getCustomer().getId());
        dto.setCustomerName(reminder.getCustomer().getAccount().getFullName()); // Assuming Customer has getName method
        dto.setReminderType(reminder.getReminderType());
        dto.setMessage(reminder.getMessage());
        dto.setIsSent(reminder.getIsSent());
        dto.setReminderDate(reminder.getReminderDate());
        return dto;
    }

}