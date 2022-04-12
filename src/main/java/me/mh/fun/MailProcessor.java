package me.mh.fun;

import com.opencsv.CSVWriter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.mail.*;
import javax.mail.internet.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class MailProcessor {

    private final String SMTP_HOST;
    private final String SMTP_PORT;
    private final String USER_ID;
    private final String PASSWORD;

    private  String mail_content="";

    private final String csvFilePath = "/tmp/secret-messages.csv";

    private static final Logger logger = LogManager.getLogger(MailProcessor.class);

    public MailProcessor(String SMTP_HOST, String SMTP_PORT, String USER_ID, String PASSWORD) {
        this.SMTP_HOST = SMTP_HOST;
        this.SMTP_PORT = SMTP_PORT;
        this.USER_ID = USER_ID;
        this.PASSWORD = PASSWORD;
    }

    public void sendAttachMail(String mailTo) throws IOException, MessagingException {
        String message = "Hi All";
        String subject = "A secret message";
        Properties props = new Properties();
        props.put("mail.debug", "true");
        props.put("mail.smtp.auth", true);
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", SMTP_HOST);
        props.put("mail.smtp.ssl.trust", SMTP_HOST);
        props.put("mail.smtp.port", SMTP_PORT);
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(USER_ID, PASSWORD);
            }
        });
        session.setDebug(true);
        MimeMessage msg = new MimeMessage(session);
        List<String> emails = getEmails(mailTo);
        msg.setFrom(new InternetAddress(USER_ID));
        msg.setRecipients(Message.RecipientType.TO, getRecipients(emails));
        msg.setSubject(subject);
        MimeMultipart mimeMultipart = new MimeMultipart();
        MimeBodyPart textMime = new MimeBodyPart();
        MimeBodyPart fileMime = new MimeBodyPart();
        try {
            textMime.setText(message);
            File file = new File(csvFilePath);
            fileMime.attachFile(file);
            mimeMultipart.addBodyPart(textMime);
            mimeMultipart.addBodyPart(fileMime);
        } catch (Exception e) {
            e.printStackTrace();
        }
        msg.setContent(mimeMultipart, "text/html");
        msg.setSentDate(new Date());
        Transport.send(msg);
    }

    public List<String> getEmails(String mailTo) {
        return Arrays.asList(mailTo.split(","));
    }

    private InternetAddress[] getRecipients(List<String> emails) throws AddressException {
        InternetAddress[] addresses = new InternetAddress[emails.size()];
        for (int i = 0; i < emails.size(); i++) {
            addresses[i] = new InternetAddress(emails.get(i));
        }
        return addresses;
    }

    public void createCSVFile(List<String[]> csvContent) throws IOException {
        Path relativePath = Paths.get(csvFilePath);
        Path absolutePath = relativePath.toAbsolutePath();
        logger.info("Current relative path is: " + absolutePath);
        CSVWriter writer = new CSVWriter(new FileWriter(csvFilePath));
        writer.writeAll(csvContent);
        writer.flush();
        logger.info("File Created Successfully...");
    }

    public void setMailContent() {
        mail_content= "<i>Hi All</i><br><b>Here we have attached all your secret messages.</b>";
    }
}
