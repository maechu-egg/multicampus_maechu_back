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
    private final String FROM_ADDRESS = "leeyjmaechu@gmail.com"; // 발신자 이메일 주소 명시적으로 설정

    public boolean sendCertificationMail(String email, String certificationNumber){
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(message, false, "UTF-8"); // multipart 옵션 false, UTF-8 설정

            String htmlContent = getCertificationMessage(certificationNumber);

            // 발신자 이메일 설정
            messageHelper.setFrom(FROM_ADDRESS); 
            messageHelper.setTo(email);
            messageHelper.setSubject(SUBJECT);
            messageHelper.setText(htmlContent, true);

            // 메시지 헤더에 UTF-8 인코딩 설정 추가
            message.setHeader("Content-Type", "text/html; charset=UTF-8");

            javaMailSender.send(message);
            return true;
        } catch(Exception e) {
            log.error("Failed to send email to {}: {}", email, e.getMessage());
            return false;
        }
    }

    private String getCertificationMessage(String certificationNumber) {
        return String.format("""
            <!DOCTYPE html>
            <html lang="ko">
            <head>
                <meta charset="UTF-8">
                <title>인증 메일</title>
            </head>
            <body>
                <h1 style='text-align: center;'>[WORKSPACE] 인증 메일</h1>
                <h3 style='text-align: center;'>인증 코드 : <strong style='font-size:32px; letter-spacing:8px;'>%s</strong></h3>
            </body>
            </html>
            """, certificationNumber);
    }
}
