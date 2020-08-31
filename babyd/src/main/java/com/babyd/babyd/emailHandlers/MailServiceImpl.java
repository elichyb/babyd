/*
 * ----------------------------------------------------------------------------
 *  (C) Copyright Elichy Barak 2020
 *
 *  The source code for this program is not published or other-
 * wise divested of its trade secrets, irrespective of what has
 * been deposited with the U.S. Copyright Office.
 * ----------------------------------------------------------------------------
 */

package com.babyd.babyd.emailHandlers;

import org.springframework.stereotype.Service;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.*;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

@Service
public class MailServiceImpl implements MailService{

    @Override
    public void sendMailRegisterSuccessfully(String to, String name) {
        String msg = String.format("Thanks %s,\nfor join to Baby-D app," +
                "\n" +
                "Hope you will join to use this app\n" +
                "If you have any issues/suggestion you can contact us directly\n" +
                "in mail babyd.team@gmail.com\n" +
                "Enjoy, \n" +
                "Baby-D team", name);
        send(to, msg);
    }

    @Override
    public void sendMailBabyAdded(String to, String baby_name) {
        String msg = String.format("Baby %s\n"+
                "Added successfully,\n" +
                "Thanks,\n"+
                "Baby-D team", baby_name);
        send(to, msg);
    }

    public static void send(String to, String msg){
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
            message.setText(msg);

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
