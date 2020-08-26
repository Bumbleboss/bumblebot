package xyz.bumbleboss.bumblebot;

public class configValueMissingException extends Exception {
    public configValueMissingException(String output){
        System.out.println(String.format("[Warning] Some required keys missing from Config file. \n %s", output));
    }
}
