package xyz.bumbleboss.exceptions;

public class validateFailedException extends Exception {
  
  private static final long serialVersionUID = 1L;

  public validateFailedException(String path, String output) {
    System.out.printf("[Warning] Some required values missing from '%s'. \n %s%n", path, output);
  }
}
