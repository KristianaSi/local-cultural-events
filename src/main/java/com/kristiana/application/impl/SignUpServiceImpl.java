package com.kristiana.application.impl;

import com.kristiana.application.contract.SignUpService;
import com.kristiana.application.contract.UserService;
import com.kristiana.application.dto.AddDtoUser;
import com.kristiana.application.exception.EmailException;
import com.kristiana.application.exception.SignUpException;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Properties;
import java.util.function.Supplier;
import jakarta.mail.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class SignUpServiceImpl implements SignUpService {

    private final String emailFrom;
    private static final int VERIFICATION_CODE_EXPIRATION_MINUTES = 1;

    private LocalDateTime codeCreationTime;
    private final UserService userService;
    private final Logger LOGGER = LoggerFactory.getLogger(SignUpServiceImpl.class);

    public SignUpServiceImpl(UserService userService, String emailFrom) {
        this.userService = userService;
        this.emailFrom = emailFrom;
    }

    @Override
    public void signUp(AddDtoUser addDtoUser, Supplier<String> waitForUserInput) {
        String verificationCode = generateAndSendVerificationCode(addDtoUser.email());
        String userInputCode = waitForUserInput.get();
        verifyCode(userInputCode, verificationCode);
        userService.create(addDtoUser);
    }

    private void sendVerificationCodeEmail(String email, String verificationCode) {

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.example.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props);


        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(emailFrom));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
            message.setSubject("Код підтвердження");
            message.setText("Ваш код підтвердження: " + verificationCode);
            Transport.send(message);
            LOGGER.info("Повідомлення успішно відправлено.");
        } catch (MessagingException e) {
            throw new EmailException("Помилка при відправці електронного листа: " + e.getMessage());
        }
    }

    private String generateAndSendVerificationCode(String email) {
        String verificationCode = String.valueOf((int) (Math.random() * 900000 + 100000));
        sendVerificationCodeEmail(email, verificationCode);
        codeCreationTime = LocalDateTime.now();
        return verificationCode;
    }

    private void verifyCode(String inputCode, String generatedCode) {
        if (codeCreationTime == null) {
            throw new SignUpException("Час верифікації вийшов. Спробуйте ще раз.");
        }

        LocalDateTime currentTime = LocalDateTime.now();
        long minutesElapsed = ChronoUnit.MINUTES.between(codeCreationTime, currentTime);

        if (minutesElapsed > VERIFICATION_CODE_EXPIRATION_MINUTES) {
            throw new SignUpException("Час верифікації вийшов. Спробуйте ще раз.");
        }

        if (!inputCode.equals(generatedCode)) {
            throw new SignUpException("Невірний код підтвердження.");
        }

        codeCreationTime = null;
    }
}
