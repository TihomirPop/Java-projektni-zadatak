package hr.java.projekt.util;

import java.util.List;

public class Hash {
    public static Long hash(String string){
        string = insertSalt(string);
        Long key = string.chars().mapToLong(n -> (long)n).sum();
        key = (key * key * key * key * key * key + key * key / 17) % Long.MAX_VALUE;
        return key;
    }

    private static String insertSalt(String string){
        String temp = "s";
        temp += string.substring(0, string.length()/3);
        temp += 'a';
        temp += string.substring(string.length()/3 + 1, string.length()*2/3);
        temp += 'l';
        temp += string.substring(string.length()*2/3 + 1);
        temp += 't';
        return temp;
    }
}