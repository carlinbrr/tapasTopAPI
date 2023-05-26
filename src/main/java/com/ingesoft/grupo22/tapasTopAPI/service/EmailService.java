package com.ingesoft.grupo22.tapasTopAPI.service;

public interface EmailService {
    void sendEmail(String email, String textHtml, String subject);
}
