package hr.java.projekt.threads;

import hr.java.projekt.util.EmailVerification;
import javafx.concurrent.Task;

public class SendVerificationEmailThread extends Task<Void> {
    private String email;
    private String kod;

    public SendVerificationEmailThread(String email, String kod) {
        this.email = email;
        this.kod = kod;
    }

    @Override
    public Void call() throws Exception {
        EmailVerification.sendMail(email, kod);
        return null;
    }
}
