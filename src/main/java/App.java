import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDateTime;

public class App {

    private static final Logger log = LogManager.getLogger(App.class);

    public static void main(String[] args) {
        log.info("I AM RUNNING FROM THE JAVA APP. The time is {}", LocalDateTime.now());
    }
}
