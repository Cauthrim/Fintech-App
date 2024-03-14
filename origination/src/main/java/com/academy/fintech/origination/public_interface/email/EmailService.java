package com.academy.fintech.origination.public_interface.email;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
public class EmailService {
    private static final String USERNAME = "mock username";
    private static final String PASSWORD = "mock password";
    private static final String CONTENT_TYPE = "text/html; charset=utf-8";

    public void sendEmail(String message, String subject, String clientEmail) {
        Session session = getConfiguredMailSession();
        Message mailMessage = new MimeMessage(session);
        try {
            mailMessage.setFrom(new InternetAddress("mock@mail.com"));
            mailMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(clientEmail));
            mailMessage.setSubject(subject);

            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setContent(message, CONTENT_TYPE);
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(mimeBodyPart);

            mailMessage.setContent(multipart);
            // mail sending is commented out because it's functionality is out of the project's scope
            // Transport.send(mailMessage);
        } catch (MessagingException exception) {
            throw new RuntimeException(exception);
        }
    }

    private Session getConfiguredMailSession() {
        Properties prop = new Properties();
        prop.put("mail.smtp.auth", true);
        prop.put("mail.smtp.starttls.enable", "true");
        prop.put("mail.smtp.host", "smtp.mailtrap.io");
        prop.put("mail.smtp.port", "25");
        prop.put("mail.smtp.ssl.trust", "smtp.mailtrap.io");

        return Session.getInstance(prop, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(USERNAME, PASSWORD);
            }
        });
    }
}
