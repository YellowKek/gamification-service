package com.business.money.util;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.util.Properties;

public class EmailSender {
    public static void sendEmail(String to, String subject, String content) {
        final String username = "${email.username}";
        final String password = "${email.password}";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            // Создание сообщения
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("damirgarifullin73@yandex.ru"));
            message.setSubject("Тестовое письмо");
            message.setText("Привет! Это тестовое письмо, отправленное с Java.");

            // Отправка
            Transport.send(message);
            System.out.println("Письмо успешно отправлено!");

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
