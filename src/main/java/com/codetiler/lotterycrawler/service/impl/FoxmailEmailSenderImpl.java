package com.codetiler.lotterycrawler.service.impl;

import com.codetiler.lotterycrawler.service.EmailSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Slf4j
@Service
@RequiredArgsConstructor
public class FoxmailEmailSenderImpl implements EmailSender {

    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String from;

    @Override
    public void send(String to, String subject, String content) {
        MimeMessage message=javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper=new MimeMessageHelper(message,true);
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content);
            javaMailSender.send(message);
            log.info("send email message successfully.");

        } catch (MessagingException e) {
            log.error("send email message failed,",e.getMessage(),e);
        }
    }
}
