package com.ankush.poc.service;

import com.ankush.poc.entity.Confirmation;
import com.ankush.poc.entity.User;
import com.ankush.poc.payload.EmailSchedulerRequest;
import com.ankush.poc.payload.EmailSchedulerResponse;
import com.ankush.poc.repository.ConfirmationRepository;
import com.ankush.poc.repository.UserRepository;
import com.ankush.poc.service.interfaces.EmailSenderService;
import com.ankush.poc.service.interfaces.JobScheduleService;
import com.ankush.poc.service.interfaces.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ConfirmationRepository confirmationRepository;

    @Autowired
    private EmailSenderService emailSenderService;

    @Autowired
    private JobScheduleService jobScheduleService;

    @Override
    public User registerUser(User user) {
        if(userRepository.existsByEmailIgnoreCase(user.getEmail()))
            throw new RuntimeException("Email Already Exists");
        user.setEnabled(false);
        User saved = userRepository.save(user);
        log.info("Saved User : {}",saved.toString());
        Confirmation confirmation = Confirmation.builder()
                .user(user)
                .token(UUID.randomUUID().toString())
                .createdOn(LocalDateTime.now())
                .build();
        Confirmation savedConfimration = confirmationRepository.save(confirmation);
        log.info("Saved Confimration : {}",savedConfimration.toString());
        emailSenderService.sendHtmlEmailWithEmbeddedFiles(user.getName(), user.getEmail(), confirmation.getToken());
        return saved;
    }

    @Override
    public EmailSchedulerResponse confirmUser(String token) {
        Confirmation confirmation = confirmationRepository.findByToken(token);
        if(confirmation == null)
            throw new RuntimeException("No confirmation found for the token");

        log.info("Confirmation Found : {}", confirmation.toString());
        User user = userRepository.findByEmailIgnoreCase(confirmation.getUser().getEmail());
        log.info("User Found while confirming : {}", user.toString());
        user.setEnabled(true);
        user.setUpdated_on(LocalDateTime.now());
        userRepository.save(user);
        EmailSchedulerResponse response = jobScheduleService.scheduleEmailJob(EmailSchedulerRequest.builder()
                .subject("Thanks for Choosing Us")
                .body("We at Business.com want to express our sincere gratitude for choosing us! We truly appreciate your trust and support, and we're committed to exceeding your expectations.")
                .email(user.getEmail())
                .dateTime(user.getUpdated_on().plusMinutes(30))
                        .timeZone(ZoneId.of("Asia/Kolkata"))
                .build());
        return response;
    }

    public static void main(String[] args) {
        // Get the current time in the Indian time zone
        ZoneId indianTimeZone = ZoneId.of("Asia/Kolkata");
        ZonedDateTime indianDateTime = ZonedDateTime.now(indianTimeZone);

        // Display the current time in the Indian time zone
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        System.out.println("Current time in India: " + indianDateTime.format(formatter));

        // Example of converting a LocalDateTime to ZonedDateTime with Indian time zone
        LocalDateTime localDateTime = LocalDateTime.of(2024, 2, 29, 4, 30);
        ZonedDateTime convertedDateTime = localDateTime.atZone(indianTimeZone);
        System.out.println("Converted time in India: " + convertedDateTime.format(formatter));
    }

}
