package hr.java.projekt.threads;

import hr.java.projekt.exceptions.EmailException;
import hr.java.projekt.main.Main;
import hr.java.projekt.util.EmailVerification;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SendVerificationEmailThread extends Task<Void> {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    private String email;
    private String kod;

    public SendVerificationEmailThread(String email, String kod) {
        this.email = email;
        this.kod = kod;
    }

    @Override
    public Void call() throws Exception {
        try {
            EmailVerification.sendMail(email, kod);
        } catch (EmailException e){
            logger.error(e.getMessage(), e);
            e.printStackTrace();
        }
        return null;
    }
}
