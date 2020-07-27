package com.babyd.babyd.emailHandlers;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.*;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

@Service
public class MailServiceImpl implements MailService{

    @Override
    public void sendMail(String to, String name) {

        // Set required configs
        String from = "babyd.team@gmail.com";
        String host = "smtp.gmail.com";
        String port = "587";
        String user = "babyd.team@gmail.com";
        String password = "OpenU2020";

        // Set system properties
        Properties properties = System.getProperties();
        properties.put("mail.smtp.auth", "true");
        properties.setProperty("mail.smtp.host", host);
        properties.setProperty("mail.smtp.port", port);
        properties.setProperty("mail.smtp.user", user);
        properties.setProperty("mail.smtp.password", password);
        properties.setProperty("mail.smtp.starttls.enable", "true");

        // Get the default Session object.
        Session session = Session.getDefaultInstance(properties);

        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);

            // Set from email address
            message.setFrom(new InternetAddress(from, "Babyd.team"));

            // Set the recipient email address
            message.addRecipient(MimeMessage.RecipientType.TO, new InternetAddress(to));

            // Set email subject
            message.setSubject("Thanks for registration!");

            // Set email body
            message.setText(String.format("Thanks %s\n for join to Baby-D app," +
                    "\n" +
                    "Hope you will join to use this app\n" +
                    "If you have any issues/suggestion you can contact us directly\n" +
                    "in mail babyd.team@gmail.com\n" +
                    "Enjoy, \n" +
                    "Baby-D team", name));

            // Set configs for sending email
            Transport transport = session.getTransport("smtp");
            transport.connect(host, from, password);

            // Send email
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (AddressException e) {
            e.printStackTrace();
        } catch (javax.mail.MessagingException e) {
            e.printStackTrace();
        }

    }
}
