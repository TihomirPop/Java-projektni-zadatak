package hr.java.projekt.util;

import hr.java.projekt.exceptions.EmailException;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
public class EmailVerification {
    private static final String EMAIL_VERIFICATION_FILE = "emailVerification.properties";
    public static synchronized void sendMail(String email, String kod) throws EmailException{
        try {
            Properties properties = System.getProperties();
            Properties emailProperties = new Properties();
            emailProperties.load(new FileReader(EMAIL_VERIFICATION_FILE));

            String host = "smtp.gmail.com";


            properties.put("mail.smtp.host", host);
            properties.put("mail.smtp.port", "465");
            properties.put("mail.smtp.ssl.enable", "true");
            properties.put("mail.smtp.auth", "true");
            Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(emailProperties.getProperty("email"), emailProperties.getProperty("password"));
                }
            });

            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(emailProperties.getProperty("email")));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
            message.setSubject("E-mail potvrda");
            message.setContent(
                    "<h2 style=\"text-align: center;\">E-mail potvrda</h2>" +
                            "<h3 style=\"text-align: center;\">Unesite kod za verifikaciju u aplikaciju:</h3>\n" +
                            "<h1 style=\"text-align: center;\">" + kod + "</h1>",
                    "text/html");
            Transport.send(message);
        } catch (MessagingException | IOException e) {
            throw new EmailException(e);
        }
    }
}
