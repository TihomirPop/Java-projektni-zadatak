package hr.java.projekt.util;

import hr.java.projekt.entitet.User;
import hr.java.projekt.exceptions.DatotekaException;
import hr.java.projekt.main.Main;
import javafx.fxml.FXMLLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalLong;
import java.util.stream.Collectors;

public class Datoteke {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static final String USERS_PATH = "dat/users.txt";
    public static final int SIZE_OF_USERS = 6;

    public static List<User> getUsers() throws DatotekaException{
        try(BufferedReader reader = new BufferedReader(new FileReader(USERS_PATH))) {
            List<String> usersLines = reader.lines().collect(Collectors.toList());
            List<User> users = new ArrayList<>();
            for(int i = 0; i < usersLines.size(); i += SIZE_OF_USERS){
                users.add(new User(
                        Long.parseLong(usersLines.get(i)),
                        usersLines.get(i + 1),
                        usersLines.get(i + 2),
                        Long.parseLong(usersLines.get(i + 3)),
                        Integer.parseInt(usersLines.get(i + 4)),
                        Boolean.parseBoolean(usersLines.get(i + 5))
                ));
            }
            return users;
        }catch (IOException e){
            throw new DatotekaException(e);
        }
    }

    public static void addUser(User user) throws DatotekaException{
        try (BufferedWriter out = new BufferedWriter(new FileWriter(USERS_PATH, true))) {
            OptionalLong optionalId = getUsers().stream().mapToLong(p -> p.getId()).max();
            Long id = optionalId.getAsLong() + 1;
            out.write('\n' + id.toString());
            out.write('\n' + user.getEmail());
            out.write('\n' + user.getUsername());
            out.write('\n' + user.getPassword().toString());
            out.write('\n' + user.getRole().toString());
            out.write('\n' + user.getVerified().toString());
            Main.prikaziScene(new FXMLLoader(Main.class.getResource("login.fxml")));
        } catch (IOException e) {
            throw new DatotekaException(e);
        }
    }

    public static void editUser(User user) throws DatotekaException{
        try {
            List<String> userLines = Files.lines(Path.of(USERS_PATH)).collect(Collectors.toList());
            for(int i = 0; i < userLines.size(); i += SIZE_OF_USERS)
                if(userLines.get(i).equals(user.getId().toString())){
                    if(user.getEmail() != null)
                        userLines.set(i + 1, user.getEmail());
                    if(user.getUsername() != null)
                        userLines.set(i + 2, user.getUsername());
                    if(user.getPassword() != null)
                        userLines.set(i + 3, user.getPassword().toString());
                    if(user.getRole() != null)
                        userLines.set(i + 4, user.getRole().toString());
                    if(user.getVerified() != null)
                        userLines.set(i + 5, user.getVerified().toString());
                    break;
                }
            String userText = userLines.get(0);
            for(int i = 1; i < userLines.size(); i++)
                userText += '\n' + userLines.get(i);
            
            Files.write(Path.of(USERS_PATH), userText.getBytes());
        } catch (IOException e) {
            throw new DatotekaException(e);
        }

    }

    public static void deleteUser(User user) throws DatotekaException{
        List<String> usersLines = null;
        try {
            usersLines = Files.lines(Path.of(USERS_PATH)).collect(Collectors.toList());
        } catch (IOException e) {
            throw new DatotekaException(e);
        }

        try(BufferedWriter out = new BufferedWriter(new FileWriter(USERS_PATH, false))){
            for(int i = 0; i < usersLines.size(); i += SIZE_OF_USERS){
                if(Long.parseLong(usersLines.get(i)) == user.getId())
                    continue;
                if(i == 0){
                    out.write(usersLines.get(i) + '\n');
                    out.write(usersLines.get(i + 1) + '\n');
                    out.write(usersLines.get(i + 2) + '\n');
                    out.write(usersLines.get(i + 3) + '\n');
                    out.write(usersLines.get(i + 4) + '\n');
                    out.write(usersLines.get(i + 5));
                    continue;
                }
                out.write('\n' + usersLines.get(i));
                out.write('\n' + usersLines.get(i + 1));
                out.write('\n' + usersLines.get(i + 2));
                out.write('\n' + usersLines.get(i + 3));
                out.write('\n' + usersLines.get(i + 4));
                out.write('\n' + usersLines.get(i + 5));
            }

        }catch (IOException e){
            throw new DatotekaException(e);
        }
    }
}
