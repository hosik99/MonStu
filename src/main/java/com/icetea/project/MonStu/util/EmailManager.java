package com.icetea.project.MonStu.util;

import com.icetea.project.MonStu.enums.EmailMsg;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
public class EmailManager {

    //Site Main Email
    @Value("${main.email}")
    private String mainEmailAddress;

    private JavaMailSender mailSender;

    public EmailManager(JavaMailSender mailSender){
        this.mailSender = mailSender;
    }

    //Send Check Email Code When signIn
    public String sendEmailCheck(String email) {
        String randomCode = getRandomCode();
        try {
            sendEmail(email, EmailMsg.SIGNUP_SUBJECT.getMessage(),EmailMsg.SIGNUP_CONTENT.getMessage(randomCode));
            log.info(" Email: "+email+"--- Code transfer successful");
            return randomCode;
        }catch (Exception e){
            log.info(" Email: "+email+"--- Code transfer failed ---" + e.getMessage());
            return null;
        }
    }
    //SimpleMailMessage-> text만 , MimeMessage -> html 적용가능
    public void sendEmail(String to, String subject, String content) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true); // true for multipart
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true); // true indicates that the content is HTML
            helper.setFrom(mainEmailAddress);

            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private String getRandomCode(){
        UUID randomUUID = UUID.randomUUID();
        return randomUUID.toString().replace("-","").substring(0, 6);
    }

}
