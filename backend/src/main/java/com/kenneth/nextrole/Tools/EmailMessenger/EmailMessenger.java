package com.kenneth.nextrole.Tools.EmailMessenger;


import com.kenneth.nextrole.Model.PasswordResetCode;
import com.kenneth.nextrole.Repository.PasswordResetRespository;
import lombok.Setter;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Service
public class EmailMessenger {
    @Setter
    private MailSender mailSender;
    private final SimpleMailMessage msg;
    private final PasswordResetRespository repo;

    public EmailMessenger(SimpleMailMessage msg, MailSender sender, PasswordResetRespository repo) {
        this.msg = msg;
        this.mailSender = sender;
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
        msg.setFrom("nextroleadmin@gmail.com");
        msg.setTo(email);
        msg.setSubject("Requested NextRole Password Reset");
        msg.setText("Here is your code: " + code + "\n" +"This code will expire in 20 minutes. After that you will need to request this code again");

        PasswordResetCode resetCode = repo.findByEmail(email).orElseGet(PasswordResetCode::new);
        resetCode.setEmail(email);
        resetCode.setCode(code);
        resetCode.setExpiresAt(LocalDateTime.now().plusMinutes(20));
        repo.save(resetCode);
        this.mailSender.send(msg);
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
