/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

/**
 *
 * @author PC
 */
public class TextConvert {

    public static String convertToDatabaseFormat(String text) {
        try {
            if (text == null) {
                return null;
            }
            // Replace newline characters with "\n" for database storage
            return text.replace("\r\n", "\n").replace("\r", "\n").replace(System.lineSeparator(), "\n");
        } catch (Exception e) {
            // Log the exception (replace with your preferred logging mechanism)
            System.err.println("Error in convertToDatabaseFormat: " + e.getMessage());
            return null; // Or return the original text, or an empty string, depending on desired behavior
        }
    }

    public static String convertToHTMLFormat(String text) {
        try {
            if (text == null) {
                return null;
            }
            // Replace "\n" with <br> tags for HTML display
            return text.replace("\n", "<br/>");
        } catch (Exception e) {
            System.err.println("Error in convertToHTMLFormat: " + e.getMessage());
            return null; // Or return the original text, or an empty string
        }
    }

    public static String convertToTextFormat(String text) {
        try {
            if (text == null) {
                return null;
            }
            // Replace "\n" with paragraph breaks for display in textarea
            return text.replace("\n", System.lineSeparator());
        } catch (Exception e) {
            System.err.println("Error in convertToTextFormat: " + e.getMessage());
            return null;  // Or return the original text
        }
    }
}
