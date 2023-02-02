package hr.java.projekt.threads;

import hr.java.projekt.entitet.Promjena;
import hr.java.projekt.exceptions.PromjeneException;
import hr.java.projekt.util.Datoteke;
import hr.java.projekt.util.Promjene;

public class AddPromjenaThread implements Runnable{
    private Promjena promjena;

    public AddPromjenaThread(Promjena promjena) {
        this.promjena = promjena;
    }
    @Override
    public void run() {
        try {
            Promjene.addPromjena(promjena);
        } catch (PromjeneException e) {
            throw new RuntimeException(e);
        }
    }
}
