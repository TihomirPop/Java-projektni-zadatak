package hr.java.projekt.util;

import hr.java.projekt.entitet.Promjena;
import hr.java.projekt.exceptions.PromjeneException;

import java.io.*;
import java.util.List;

public class Promjene {
    public static final String PROMJENE_PATH = "dat/promjene.dat";
    public static List<Promjena> getPromjene() throws PromjeneException {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(PROMJENE_PATH))) {
            return (List<Promjena>) in.readObject();
        } catch (ClassNotFoundException | IOException e) {
            throw new PromjeneException(e);
        }
    }
    public static void addPromjena(Promjena promjena) throws PromjeneException {
        List<Promjena> promjene = getPromjene();
        promjene.add(promjena);
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(PROMJENE_PATH))) {
            out.writeObject(promjene);
        } catch (IOException e) {
            throw new PromjeneException(e);
        }
    }
}
