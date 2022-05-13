package org.example.service;

import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Service
public class EmailService {

    //Sending Email with Message...
    public boolean sendEmail(String subject, String message, String to) {

        boolean flag=false;
        //Variable for gmail
        String host="smtp.gmail.com";

        String from="shivangtripathi370@gmail.com";

        Properties properties=System.getProperties();

        //Setting Important information to properties object

        //Host Set
        properties.put("mail.smtp.host",host);
        properties.put("mail.smtp.port",465);
        properties.put("mail.smtp.ssl.enable",true);
        properties.put("mail.smtp.auth",true);

        //Step 1: to get Session Object
        Session session=Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("shivangtripathi370@gmail.com","tbelqozgeaexsyl");
            }
        });
        session.setDebug(true);

        //Step 2: Compose the Message (text,Multi-media)
        MimeMessage mimeMessage=new MimeMessage(session);

        try{
            //setting from
            mimeMessage.setFrom(from);

            //setting recipient
            mimeMessage.addRecipient(Message.RecipientType.TO,new InternetAddress(to));

            //setting Subject
            mimeMessage.setSubject(subject);

            //setting text message to be send
            mimeMessage.setText(message);

            //Step 3: Send message using Transport Class
            Transport.send(mimeMessage);

            System.out.println("**************** Sent Successfully ******************");
            flag=true;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }
}
