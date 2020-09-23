package xyz.bumbleboss.exceptions;

public class validateFailedException extends Exception {
    public validateFailedException(String path, String output){
        System.out.println(String.format("[Warning] Some required values missing from '%s'. \n %s", path, output));
    }
}
