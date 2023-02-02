package hr.java.projekt.threads;

import hr.java.projekt.entitet.Promjena;
import hr.java.projekt.exceptions.PromjeneException;
import hr.java.projekt.util.Promjene;

import java.util.List;

public class AddPromjeneThread implements Runnable{
    private List<Promjena> promjene;

    public AddPromjeneThread(List<Promjena> promjene) {
        this.promjene = promjene;
    }
    @Override
    public void run() {
        try {
            Promjene.addPromjene(promjene);
        } catch (PromjeneException e) {
            throw new RuntimeException(e);
        }
    }
}
