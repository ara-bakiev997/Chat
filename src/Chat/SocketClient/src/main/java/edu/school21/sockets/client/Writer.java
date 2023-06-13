package edu.school21.sockets.client;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Scanner;
import edu.school21.sockets.models.JsonObject;
import edu.school21.sockets.services.jsonservice.JsonService;
import edu.school21.sockets.services.jsonservice.JsonServiceImpl;

public class Writer implements Runnable {
  private final BufferedWriter writer;
  private final Scanner scanner = new Scanner(System.in);
  private final JsonService jsonService = new JsonServiceImpl();

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
        JsonObject jsonObject = new JsonObject(userInput);
        String jsonString = jsonService.createJsonString(jsonObject);

        writer.write(jsonString + "\n");
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
