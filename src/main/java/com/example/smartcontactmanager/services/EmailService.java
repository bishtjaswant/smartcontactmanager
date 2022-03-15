package com.example.smartcontactmanager.services;

import org.springframework.stereotype.Service;

import javax.mail.*;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Service
public class EmailService{
        public static boolean sendEmail(String to, String subject, String body){
             boolean isSent = false;
          /*  // Recipient's email ID needs to be mentioned.
            String to = "sendToAddress@example.com";//change accordingly
*/
            try {
            // Sender's email ID needs to be mentioned

             String from = "jaswantbishtfuck@gmail.com";//change accordingly

            final String username = "jhghjnb@gmail.com";//your email here
            final String password = "soijnnbvcvbn";//your email pass

            // GMail's SMTP server
            String host = "smtp.gmail.com";

            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", host);
            props.put("mail.smtp.port", "587");
            props.put("mail.smtp.ssl.trust", host);

            // Get the Session object.
            Session session = Session.getInstance(props,
                    new javax.mail.Authenticator() {
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(username, password);
                        }
                    });


                // Create a default MimeMessage object.
                Message message = new MimeMessage(session);

                // Set From: header field of the header.
                message.setFrom(new InternetAddress(from));

                // Set To: header field of the header.
                message.setRecipients(Message.RecipientType.TO,
                        InternetAddress.parse(to));

                // Set Subject: header field
                message.setSubject(subject);

                // Now set the actual message
                message.setText(body);

                // Send message
                Transport.send(message);

                  isSent = true;
            } catch (Exception e) {
                isSent = false;
                e.printStackTrace();
                throw new RuntimeException(e);
            }


            return isSent;
        }

    }

