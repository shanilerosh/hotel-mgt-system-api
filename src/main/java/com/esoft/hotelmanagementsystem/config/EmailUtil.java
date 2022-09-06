package com.esoft.hotelmanagementsystem.config;

import com.esoft.hotelmanagementsystem.dto.EmailDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;

/**
 * @author ShanilErosh
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class EmailUtil {


    private final JavaMailSender mailSender;

    //Utility class to send email
    public void sendEmail(EmailDto emailDto) {

        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setText(emailDto.getEmailBody(), true);
            helper.setTo(emailDto.getToEmail());
            helper.setSubject(emailDto.getSubject());
            helper.setFrom("shanilersohtech@gmail.com");

            mailSender.send(mimeMessage);
        } catch (MailException | MessagingException e) {
            log.error(e.getLocalizedMessage(), e.getCause());
        }

    }
}
