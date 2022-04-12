package me.mh.fun;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.mail.MessagingException;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class App {

    private static final Logger log = LogManager.getLogger(App.class);

    private final static String cer = System.getenv("CERT_PATH");
    private final static String smtpHost = System.getenv("SMTP_HOST");
    private final static String smtpPort = System.getenv("SMTP_PORT");
    private final static String userId = System.getenv("USER_ID");
    private final static String password = System.getenv("PASSWORD");
    private final static String mailTo = System.getenv("MAIL_TO"); // comma seperated email addresses

    public static void main(String[] args) throws MessagingException, IOException {

        App app = new App();

        for (int i = 0; i < 10; i++) {
            log.info("I AM RUNNING FROM THE JAVA APP. The time is {}", LocalDateTime.now());
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        MailProcessor mailProcessor = new MailProcessor(smtpHost, smtpPort, userId, password);
        mailProcessor.createCSVFile(app.getCSVContents(cer));
        mailProcessor.setMailContent();
        mailProcessor.sendAttachMail(mailTo);
        System.exit(0);
    }

    private List<String[]> getCSVContents(String cerPath) throws IOException {
        List<String[]> csvContents = new ArrayList<>();
        String[] header = {"key", "value"};
        csvContents.add(header);
        List<String> lines = Files.readAllLines(Paths.get(cerPath), StandardCharsets.UTF_8);
        String readContent = String.join(System.lineSeparator(), lines);
        Gson gson = new Gson();
        Type type = new TypeToken<Map<String, String>>(){}.getType();
        Map<String, String> myMap = gson.fromJson(readContent, type);
        myMap.forEach( (key, val) -> {
            String[] value = {key, val};
            csvContents.add(value);
        });
        return csvContents;
    }
}
