package hr.java.projekt.util;

import hr.java.projekt.entitet.Promjena;
import hr.java.projekt.exceptions.PromjeneException;
import java.io.*;
import java.util.List;

public class Promjene {
    private static final String PROMJENE_PATH = "dat/promjene.dat";
    public synchronized static List<Promjena> getPromjene() throws PromjeneException {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(PROMJENE_PATH))) {
            return (List<Promjena>) in.readObject();
        } catch (ClassNotFoundException | IOException e) {
            throw new PromjeneException(e);
        }
    }
    public synchronized static void addPromjena(Promjena promjena) throws PromjeneException {
        List<Promjena> promjene = getPromjene();
        promjena.setId((long) (promjene.size() + 1));
        promjene.add(promjena);

        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(PROMJENE_PATH))) {
            out.writeObject(promjene);
        } catch (IOException e) {
            throw new PromjeneException(e);
        }
    }
    public synchronized static void addPromjene(List<Promjena> novePromjene) throws PromjeneException{
        List<Promjena> promjene = getPromjene();
        for(Promjena promjena: novePromjene){
            promjena.setId((long) (promjene.size() + 1));
            promjene.add(promjena);
        }

        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(PROMJENE_PATH))) {
            out.writeObject(promjene);
        } catch (IOException e) {
            throw new PromjeneException(e);
        }
    }
}
