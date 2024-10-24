package com.multipjt.multi_pjt.jwt;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class EmailProvider {
    private final JavaMailSender javaMailSender;

    private final String SUBJECT = "[WORKSPACE] 인증 메일";

    public boolean sendCertificationMail(String email, String certificationNumber){
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(message, true); 

            String htmlContent = getCertificationMessage(certificationNumber);

            messageHelper.setTo(email);
            messageHelper.setSubject(SUBJECT);
            messageHelper.setText(htmlContent, true);

            javaMailSender.send(message);
        } catch(Exception e) {
            log.error("Failed to send email to {}: {}", email, e.getMessage());
            return false;
        }
        return true;
       
    }

    private String getCertificationMessage(String certificationNumber) {
        String certificationMessage = "";
        certificationMessage += "<h1 style='text-align: center;'>[WORKSPACE] 인증 메일 </h1>";
        certificationMessage += "<h3 style='text-align: center;'>인증 코드 : <strong style='font-size:32px; letter-spacing:8px;'>" + certificationNumber + "</strong></h3>";
        return certificationMessage;

    }

}
