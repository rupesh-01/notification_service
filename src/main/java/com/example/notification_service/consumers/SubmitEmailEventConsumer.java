package com.example.notification_service.consumers;

import com.example.notification_service.dtos.SendEmailDto;
import com.example.notification_service.utils.EmailUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import java.util.Properties;

@Service
public class SubmitEmailEventConsumer {
    private ObjectMapper objectMapper;

    public SubmitEmailEventConsumer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "sendEmail", groupId = "notificationService")
    public void handleSendEmail(String message) throws JsonProcessingException {
        SendEmailDto  sendEmailDto = objectMapper.readValue(
                message,
                SendEmailDto.class
        );
        String to = sendEmailDto.getTo();
        String subject = sendEmailDto.getSubject();
        String body = sendEmailDto.getBody();

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com"); //SMTP Host
        props.put("mail.smtp.port", "587"); //TLS Port
        props.put("mail.smtp.auth", "true"); //enable authentication
        props.put("mail.smtp.starttls.enable", "true"); //enable STARTTLS

        //create Authenticator object to pass in Session.getInstance argument
        Authenticator auth = new Authenticator() {
            //override the getPasswordAuthentication method
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("ab2a.edu@gmail.com", "lpmigdpdysoriszn");
            }
        };
        Session session = Session.getInstance(props, auth);

        EmailUtil.sendEmail(session, to, subject, body);

    }
}
