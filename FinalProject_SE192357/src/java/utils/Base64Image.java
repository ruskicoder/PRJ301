/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.util.Base64;

/**
 *
 * @author PC
 */
public class Base64Image {

    private static final String FORMAT_PREFIX = "data:image/png;base64,";

    public static String encode(byte[] input) {
        String data = Base64.getEncoder().encodeToString(input);
        return FORMAT_PREFIX + data;
    }

    public static byte[] decode(String input) {
        if (!input.startsWith(FORMAT_PREFIX)) {
            throw new IllegalArgumentException("Invalid input format. Must start with: " + FORMAT_PREFIX);
        }
        String data = input.substring(FORMAT_PREFIX.length());
        return Base64.getDecoder().decode(data);
    }
}
