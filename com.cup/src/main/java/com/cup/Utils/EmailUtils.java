package com.cup.Utils;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class EmailUtils {

    @Autowired
    private JavaMailSender mailSender;

    public void sendSimpleMail(String to, String subject, String text, List<String> list) {

        log.info("Inside sendSimpleMail method\nCreating the mail message.");

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("projectmailx3112@gmail.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);

        if (list != null && list.size() > 0) {
            message.setCc(this.getCcArrayFromList(list));
        }

        log.info("Mail message configured. Initiating to send the mail");

        this.mailSender.send(message);

        log.info("Mail sent successfully");
    }

    public String[] getCcArrayFromList(List<String> list) {
        String[] cc = new String[list.size()];

        for(int i = 0; i < list.size(); ++i) {
            cc[i] = list.get(i);
        }
        return cc;
    }

    public void forgotMail(String to, String subject, String password) throws MessagingException {
        MimeMessage message = this.mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom("projectmailx3112@gmail.com");
        helper.setTo(to);
        helper.setSubject(subject);
        String htmlMsg = "<p><b>Your Login details for The Crystal Cup</b><br><b>Email: </b> " + to + " <br><b>Password: </b> " + password + "<br><a href=\"http://localhost:4200/\">Click here to login</a></p>";

        message.setContent(htmlMsg, "text/html");

        mailSender.send(message);
    }
}
