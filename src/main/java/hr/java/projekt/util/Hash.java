package hr.java.projekt.util;

import java.util.List;

public class Hash {
    public static Long hash(String string){
        string = insertSalt(string);
        List<Long> longs = string.chars().mapToLong(n -> (long)n).boxed().toList();
        Long key = 0L;
        for(int i = 0; i < longs.size(); i++)
            key += longs.get(i) * (long)Math.pow(31, longs.size() - (i + 1));
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
