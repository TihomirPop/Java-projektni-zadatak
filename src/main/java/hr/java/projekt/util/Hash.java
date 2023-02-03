package hr.java.projekt.util;

import hr.java.projekt.exceptions.HashException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.List;

public class Hash {
    public static String hash(String input, byte[] salt) {
        try {
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            byteStream.write(salt);
            byteStream.write(input.getBytes());
            byte[] valueToHash = byteStream.toByteArray();
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] byteArray = messageDigest.digest(valueToHash);
            return bytesToHex(byteArray);
        }catch (IOException | NoSuchAlgorithmException e) {
            throw new HashException(e);
        }
    }
    public static String hash(String input, String saltString) {
        try {
            byte[] salt = decodeHexString(saltString);
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            byteStream.write(salt);
            byteStream.write(input.getBytes());
            byte[] valueToHash = byteStream.toByteArray();
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] byteArray = messageDigest.digest(valueToHash);
            return bytesToHex(byteArray);
        }catch (IOException | NoSuchAlgorithmException e) {
            throw new HashException(e);
        }
    }

    public static String byteToHex(byte num) {
        char[] hexDigits = new char[2];
        hexDigits[0] = Character.forDigit((num >> 4) & 0xF, 16);
        hexDigits[1] = Character.forDigit((num & 0xF), 16);
        return new String(hexDigits);
    }

    public static String bytesToHex(byte[] bytes){
        StringBuilder hexStringBuilder = new StringBuilder();
        for (byte b : bytes)
            hexStringBuilder.append(byteToHex(b));
        return hexStringBuilder.toString();
    }

    public static byte hexToByte(String hexString) {
        int firstDigit = toDigit(hexString.charAt(0));
        int secondDigit = toDigit(hexString.charAt(1));
        return (byte) ((firstDigit << 4) + secondDigit);
    }

    private static int toDigit(char hexChar) {
        int digit = Character.digit(hexChar, 16);
        if(digit == -1)
            throw new HashException("Nepostojeci hex broj: " + hexChar);

        return digit;
    }

    public static byte[] decodeHexString(String hexString) {
        if (hexString.length() % 2 == 1)
            throw new HashException("Nije string hex brojeva");

        byte[] bytes = new byte[hexString.length() / 2];
        for (int i = 0; i < hexString.length(); i += 2)
            bytes[i / 2] = hexToByte(hexString.substring(i, i + 2));
        return bytes;
    }

    public static byte[] creatingRandomSalt() {
        byte[] salt = new byte[16];
        SecureRandom secure_random = new SecureRandom();
        secure_random.nextBytes(salt)   ;
        return salt;
    }

}
