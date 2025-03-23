package utils;

/**
 *
 * @author PC
 */

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Hash {

    public static String toSHA256(String input) {
        try {
            // Get an instance of the SHA-256 message digest.
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            // Convert the input string to a byte array.
            byte[] inputBytes = input.getBytes();

            // Calculate the hash.
            byte[] hashBytes = md.digest(inputBytes);

            // Convert the hash bytes to a hexadecimal string.
            StringBuilder hexString = new StringBuilder();
            for (byte hashByte : hashBytes) {
                String hex = Integer.toHexString(0xff & hashByte);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            System.err.println("SHA-256 algorithm not found: " + e.getMessage());
            return null;
        }
    }

}
