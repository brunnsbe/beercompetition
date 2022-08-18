package fi.homebrewing.competition.email;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class EmailSender {
    private final JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String senderEmailAddress;

    public EmailSender(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendEmail(String emailAddress, String subject, String body) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom(senderEmailAddress);
        msg.setTo(emailAddress);

        msg.setSubject(subject);
        msg.setText(body);

        javaMailSender.send(msg);
    }
}
