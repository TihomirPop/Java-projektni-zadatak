package hr.java.projekt.util;

import hr.java.projekt.entitet.User;
import hr.java.projekt.main.Main;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Datoteke {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static final String USERS_PATH = "dat/users.txt";
    public static final int SIZE_OF_USERS = 5;

    public static List<User> getUsers(){
        try(BufferedReader reader = new BufferedReader(new FileReader(USERS_PATH))) {
            System.out.println("Loading users…");
            logger.info("Loading users...");
            List<String> usersLines = reader.lines().collect(Collectors.toList());
            List<User> users = new ArrayList<>();
            for(int i = 0; i < usersLines.size(); i += SIZE_OF_USERS){
                users.add(new User(
                        Long.parseLong(usersLines.get(i)),
                        usersLines.get(i + 1),
                        usersLines.get(i + 2),
                        Long.parseLong(usersLines.get(i + 3)),
                        Integer.parseInt(usersLines.get(i + 4))
                ));
            }
            return users;
        }catch (IOException e){
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
}
