package com.kmtech.bitix_clone.service;

public interface EmailService {
    void sendSimpleEmail(String to, String subject, String text);
    void sendNotification(String to, String subject, String text);
}
