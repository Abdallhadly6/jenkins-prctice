package utility;

import org.zeroturnaround.zip.ZipUtil;

//email
import java.io.File;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;
import javax.mail.Session;
import javax.mail.Transport;

public class EmailManager {

    public static void sendMail() throws MessagingException {
        String folder = "test-reports-" + browserType;
        String zipFile = "test-reports-" + browserType + ".zip";

        ZipUtil.pack(new File(folder), new File(zipFile));
        System.out.println("Created ZIP for " + browserType + ": " + zipFile);

        //////////////////////////////////////////////////////////////////////////////////////
        String content = "Project Test Report At  " + String.valueOf(java.time.LocalDate.now()) ;
        BodyPart messageBodyPart1 = new MimeBodyPart();
        messageBodyPart1.setText(content);
        BodyPart messageBodyPart2 = new MimeBodyPart();
        String filename = "test-reports.zip";
        DataSource source = new FileDataSource(filename);
        messageBodyPart2.setDataHandler(new DataHandler(source));
        messageBodyPart2.setFileName(filename);
        Multipart multipartObject = new MimeMultipart();
        multipartObject.addBodyPart(messageBodyPart1);
        multipartObject.addBodyPart(messageBodyPart2);

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2"); // ← إضافة مهمة


        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("adlyabdallh9@gmail.com", "xldzbjqrfcpzbxdt");
            }
        });
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("adlyabdallh9@gmail.com")); //Set from address of the email
            message.setContent(multipartObject, "text/html");
            message.addRecipients(Message.RecipientType.TO, InternetAddress.parse("adlyabdallh9@gmail.com,abdallhadly8@gmail.com")); //Set email recipient
            message.setRecipients(Message.RecipientType.CC, InternetAddress.parse("abdallhadly6@gmail.com")); //Set email recipient
            message.setSubject("Our Project Report");
            Transport.send(message);
            System.out.println("sent email successfully!");
        } catch (MessagingException e) {
            throw new RuntimeException(e.getMessage());

        }
    }
}
