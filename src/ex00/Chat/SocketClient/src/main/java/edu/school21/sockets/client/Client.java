package edu.school21.sockets.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;


public class Client implements AutoCloseable {
  private String address;
  private int port;
  private Socket client;
  private Scanner scanner = new Scanner(System.in);
  private BufferedWriter writer;
  private BufferedReader reader;

  public Client(String address, int port) {
    this.address = address;
    this.port = port;
  }

  public void launch() throws UnknownHostException, IOException {
    client = new Socket(address, port);
    writer = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
    reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
    String response = "";
    String request = "";

    while (true) {
      response = readInputStream();
      System.out.println(response);

      if (response == null || "successful!".equalsIgnoreCase(response.trim())
          || "User with given name already exists".equalsIgnoreCase(response)) {
        break;
      }
      request = userInputFromConsole();
      writeOutputStream(request);
    }
  }

  private String readInputStream() throws IOException {
    return reader.readLine();
  }

  private void writeOutputStream(String message) throws IOException {
    writer.write(message + "\n");
    writer.flush();
  }

  private String userInputFromConsole() {
    String userInput = "";
    System.out.print(">");
    if (scanner.hasNext()) {
      userInput = scanner.next();
    }
    return userInput;
  }

  @Override
  public void close() throws IOException {
    if (scanner != null) {
      scanner.close();
    }
    if (client != null) {
      client.close();
    }
    if (writer != null) {
      writer.close();
    }
    if (reader != null) {
      reader.close();
    }
  }


}
