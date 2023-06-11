package edu.school21.sockets.client;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Scanner;

public class Writer implements Runnable {
  private final BufferedWriter writer;
  private final Scanner scanner = new Scanner(System.in);

  public Writer(BufferedWriter writer) {
    this.writer = writer;
  }

  @Override
  public void run() {
    try {
      while (!Thread.currentThread().isInterrupted()) {

        String userInput = "";
        if (scanner.hasNext()) {
          userInput = scanner.nextLine();
        }
        writer.write(userInput + "\n");
        writer.flush();

        if ("exit".equalsIgnoreCase(userInput)) {
          disconnect();
          break;
        }

      }
    } catch (IOException e) {
      handleIOException(e);
    } finally {
      scanner.close();
    }
  }

  public void disconnect() {
    try {
      writer.close();
    } catch (IOException e) {
      handleIOException(e);
    }
  }

  private void handleIOException(IOException e) {
    System.err.println("Error occurred in Writer: " + e.getMessage());
    disconnect();
  }
}
