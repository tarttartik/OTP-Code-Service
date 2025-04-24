package notification;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailService {

    private final String username;
    private final String password;
    private final String fromEmail;
    private final Session session;

    private static final Logger logger = LogManager.getLogger(EmailService.class);

    public EmailService() {
        Properties config = loadConfig();
        this.username = config.getProperty("email.username");
        this.password = config.getProperty("email.password");
        this.fromEmail = config.getProperty("email.from");

        this.session = Session.getInstance(config, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
    }

    private Properties loadConfig() {
        try {
            Properties props = new Properties();
            props.load(EmailService.class.getClassLoader()
                    .getResourceAsStream("email.properties"));
            return props;
        } catch (Exception e) {
            logger.error("Ошибка загрузки конфигурации");
            throw new RuntimeException("Ошибка загрузки конфигурации", e);
        }
    }

    public void sendCode(String toEmail, String code) {
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
            message.setSubject("Your OTP Code");
            message.setText("Ваш код: " + code);

            Transport.send(message);
        } catch (MessagingException e) {
            logger.error(STR."Ошибка отправки сообщения: \{e.getMessage()}");
            throw new RuntimeException("Failed to send email", e);
        }
    }
}