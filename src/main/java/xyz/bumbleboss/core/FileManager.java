package xyz.bumbleboss.core;

import java.nio.file.*;
import java.io.*;

public class FileManager {

  public static String readFile(String path) {
    String data = ""; 
    try {
      data = new String(Files.readAllBytes(Paths.get(path))); 
    } catch (IOException e) {
      e.printStackTrace();
    }
    return data; 
  }
}