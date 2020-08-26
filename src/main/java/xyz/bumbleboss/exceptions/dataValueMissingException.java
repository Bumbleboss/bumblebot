package xyz.bumbleboss.exceptions;

public class dataValueMissingException extends Exception {
    public dataValueMissingException(String path, String output){
        System.out.println(String.format("[Warning] Some required values missing from '%s'. \n %s", path, output));
    }
}
