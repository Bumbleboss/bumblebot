package xyz.bumbleboss.bumblebot;

public class configValueMissingException extends Exception {
    public configValueMissingException(String key){
        System.out.println(String.format("[Warning] Required key '%s' not found in config file", key));
    }
}
