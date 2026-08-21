package com.kenneth.nextrole.Tools.EmailMessenger;


import com.kenneth.nextrole.Model.PasswordResetCode;
import com.kenneth.nextrole.Repository.PasswordResetRespository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Service
public class EmailMessenger {

    private final JavaMailSender mailSender;
    private final PasswordResetRespository repo;

    public EmailMessenger(JavaMailSender mailSender, PasswordResetRespository repo) {
        this.mailSender = mailSender;
        this.repo = repo;
    }


    public String genCode(){
        String pool = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom random = new SecureRandom();
        return random.ints(6, 0, pool.length())
                .mapToObj(pool::charAt)
                .map(Object::toString)
                .collect(Collectors.joining());
    }

    public void sendCode(String email){

        String code = genCode();

        PasswordResetCode resetCode = repo.findByEmail(email).orElseGet(PasswordResetCode::new);
        resetCode.setEmail(email);
        resetCode.setCode(code);
        resetCode.setExpiresAt(LocalDateTime.now().plusMinutes(20));
        repo.save(resetCode);

        sendResetEmail(email, code);
    }

    private void sendResetEmail(String email, String code) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");
            helper.setFrom("nextroleadmin@gmail.com");
            helper.setTo(email);
            helper.setSubject("Requested NextRole Password Reset");
            helper.setText(plainTextBody(code), htmlBody(code));
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new MailSendException("Failed to send password reset email", e);
        }
    }

    private String plainTextBody(String code) {
        return "Here is your code: " + code + "\n"
                + "This code will expire in 20 minutes. After that you will need to request this code again";
    }

    private String htmlBody(String code) {
        return """
                <div style="font-family: Arial, Helvetica, sans-serif; max-width: 480px; margin: 0 auto; padding: 32px; color: #1B2430;">
                  <h1 style="font-size: 20px; margin: 0 0 16px;">NextRole</h1>
                  <p style="font-size: 14px; color: #4B5563; line-height: 1.5;">
                    You requested to reset your password. Use the code below to continue:
                  </p>
                  <div style="background: #F6F2E9; border-left: 4px solid #B9932A; border-radius: 8px; padding: 16px 20px; margin: 20px 0; text-align: center;">
                    <span style="font-size: 28px; font-weight: bold; letter-spacing: 6px; color: #1B2430;">%s</span>
                  </div>
                  <p style="font-size: 13px; color: #6B7280; line-height: 1.5;">
                    This code will expire in 20 minutes. After that you will need to request a new one.
                  </p>
                  <p style="font-size: 12px; color: #9CA3AF; margin-top: 24px;">
                    If you didn't request this, you can safely ignore this email.
                  </p>
                </div>
                """.formatted(code);
    }

    public boolean verify(String email, String code) {

        PasswordResetCode resetCode = repo.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("No password reset was requested for this email"));

        return code.equals(resetCode.getCode())
                && resetCode.getExpiresAt().isAfter(LocalDateTime.now());
    }

    public void invalidateCode(String email) {
        repo.deleteByEmail(email);
    }

}
