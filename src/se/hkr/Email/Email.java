package se.hkr.Email;


import se.hkr.Dialogue;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class Email {

    private final String FROM = "rentall.hkr@gmail.com";
    private final String PASSWORD = "projekt2";
    private String to;
    private String subject;
    private String content;
    private Session session;


    public Email(String to, String subject, String message) {
        this.to = to;
        this.subject = subject;
        this.content = message;

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        //get Session
        session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(FROM, PASSWORD);
                    }
                });
    }

    public void send () {
       try {
           MimeMessage message = new MimeMessage(session);
           message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
           message.setSubject(subject);
           message.setText(content);
           Transport.send(message);

       }catch (MessagingException e) {
           Dialogue.alert("OHNOOOO");
       }


    }
}