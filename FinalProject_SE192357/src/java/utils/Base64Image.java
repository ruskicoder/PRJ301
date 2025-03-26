/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;
import javax.servlet.ServletContext;

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

    public static String imageToBase64(String imagePath, ServletContext context) {
        try {
             String absolutePath = context.getRealPath(imagePath); // Get the real path
            File imageFile = new File(absolutePath);
            byte[] imageBytes = Files.readAllBytes(imageFile.toPath());
            return encode(imageBytes); // Use existing encode method
        } catch (IOException e) {
            System.err.println("Error converting image to Base64: " + e.getMessage());
            return null; 
        }
    }
}
