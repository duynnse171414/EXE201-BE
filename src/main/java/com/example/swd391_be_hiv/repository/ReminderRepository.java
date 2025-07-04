package com.example.swd391_be_hiv.repository;

import com.example.swd391_be_hiv.entity.Reminder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReminderRepository extends JpaRepository<Reminder, Long> {
    List<Reminder> findByIsSentFalseAndReminderDateLessThanEqual(LocalDate date);


    List<Reminder> findByCustomerIdAndIsSentFalse(Long id);

    List<Reminder> findByReminderType(String reminderType);

    Reminder findReminderByReminderId(Long reminderId);

    List<Reminder> findRemindersByCustomerId(Long customerId);

    List<Reminder> findRemindersByReminderType(String reminderType);

    List<Reminder> findRemindersByIsSentFalse();

    List<Reminder> findRemindersByIsSentTrue();


    List<Reminder> findRemindersByReminderDateAfter(LocalDate date);

    List<Reminder> findRemindersByReminderDateBefore(LocalDate date);

    List<Reminder> findRemindersByReminderDateBetween(LocalDate startDate, LocalDate endDate);

    List<Reminder> findRemindersByIsSentFalseAndReminderDateLessThanEqual(LocalDate date);

    List<Reminder> findRemindersByReminderTypeAndIsSentFalse(String reminderType);
}