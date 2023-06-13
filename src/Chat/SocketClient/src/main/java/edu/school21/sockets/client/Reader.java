package edu.school21.sockets.client;

import java.io.BufferedReader;
import java.io.IOException;
import edu.school21.sockets.models.JsonObject;
import edu.school21.sockets.services.jsonservice.JsonService;
import edu.school21.sockets.services.jsonservice.JsonServiceImpl;

public class Reader implements Runnable {
  private final BufferedReader reader;
  private final JsonService jsonService = new JsonServiceImpl();

  public Reader(BufferedReader reader) {
    this.reader = reader;
  }

  @Override
  public void run() {
    try {
      while (!Thread.currentThread().isInterrupted()) {

        String read = reader.readLine();
        if (read == null) {
          System.out.println("Server not responding");
          disconnect();
          break;
        }

        JsonObject jsonObject = jsonService.parseJsonString(read);

        System.out.println(jsonObject.getMessage());

        if ("You have left the chat.".equalsIgnoreCase(jsonObject.getMessage())) {
          disconnect();
          break;
        }

      }
    } catch (IOException e) {
      disconnect();
    }
  }

  public void disconnect() {
    try {
      reader.close();
      System.exit(0);
    } catch (IOException e) {
      handleIOException(e);
    }
  }

  private void handleIOException(IOException e) {
    System.err.println("Error occurred in Reader: " + e.getMessage());
    disconnect();
  }
}
