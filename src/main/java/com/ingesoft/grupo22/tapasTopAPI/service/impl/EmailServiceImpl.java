package com.ingesoft.grupo22.tapasTopAPI.service.impl;

import com.ingesoft.grupo22.tapasTopAPI.service.EmailService;
import com.sun.mail.smtp.SMTPTransport;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

import static javax.mail.Message.RecipientType.CC;
import static javax.mail.Message.RecipientType.TO;

@Service
public class EmailServiceImpl implements EmailService {

    private static final String SIMPLE_MAIL_TRANSFER_PROTOCOL = "smtp";
    private static final String CC_EMAIL = "";
    private static final String OUTLOOK_SMTP_SERVER = "smtp-mail.outlook.com";
    private static final String SMTP_HOST = "mail.smtp.host";
    private static final String SMTP_AUTH = "mail.smtp.auth";
    private static final String SMTP_PORT = "mail.smtp.port";
    private static final int DEFAULT_PORT = 587;
    private static final String SMTP_STARTTLS_ENABLE = "mail.smtp.starttls.enable";
    private static final String SMTP_STARTTLS_REQUIRED = "mail.smtp.starttls.required";
    private static final String X_PRIORITY_HEADER = "X-Priority";
    private static final String X_PRIORITY_HEADER_VALUE = "2";

    @Value("${myEmail}")
    private String username;
    @Value("${myPwd}")
    private String password;
    @Value("${myEmail}")
    private String from_email;

    @Override
    public void sendEmail(String email, String textHtml, String subject) {
        try{
            MimeMessage message = createEmail(email, textHtml, subject);
            SMTPTransport smtpTransport = (SMTPTransport) getEmailSession().getTransport(SIMPLE_MAIL_TRANSFER_PROTOCOL);
            smtpTransport.connect(OUTLOOK_SMTP_SERVER, username, password);
            smtpTransport.sendMessage(message, message.getAllRecipients());
            smtpTransport.close();
        }catch (Exception exception) {
            throw new IllegalStateException(exception.getMessage());
        }
    }

    private MimeMessage createEmail(String email, String textHtml,String subject) throws MessagingException {
        MimeMessage message = new MimeMessage(getEmailSession());
        message.setFrom(new InternetAddress(from_email));
        message.setRecipients(TO, InternetAddress.parse(email, false));
        message.setRecipients(CC, InternetAddress.parse(CC_EMAIL, false));
        message.setHeader(X_PRIORITY_HEADER, X_PRIORITY_HEADER_VALUE);
        message.setSubject(subject);
        message.setText(textHtml, "utf-8", "html");
        message.setSentDate(new Date());
        message.saveChanges();
        return message;
    }

    private Session getEmailSession() {
        Properties properties = System.getProperties();
        properties.put(SMTP_HOST, OUTLOOK_SMTP_SERVER);
        properties.put(SMTP_AUTH, true);
        properties.put(SMTP_PORT, DEFAULT_PORT);
        properties.put("mail.smtp.ssl.trust", "*");
        properties.put(SMTP_STARTTLS_REQUIRED, true);
        properties.put(SMTP_STARTTLS_ENABLE, true);
        properties.put("mail.smtp.ssl.protocols", "TLSv1.2");
        return Session.getInstance(properties, null);
    }

}
