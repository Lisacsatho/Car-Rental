package se.hkr;


import java.util.Properties;

public class Email {

    String to = "Lisacsatho@hotmail.com";//change accordingly
    String from = "Lisacsatho@hotmail.com";
    String host = "localhost";//or IP address
    String subject = "JavaMail test";
    String messageText = "Body of message";

    Properties properties = System.getProperties();

}