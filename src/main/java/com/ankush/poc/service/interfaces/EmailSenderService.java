package com.ankush.poc.service.interfaces;

import javax.mail.MessagingException;

public interface EmailSenderService {
    void sendHtmlEmailWithEmbeddedFiles(String name, String to, String token);
}
