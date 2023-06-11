package edu.school21.sockets.app;

import java.io.IOException;
import edu.school21.sockets.client.Client;

public class Main {
  public static void main(String[] args) {
    String inputPortPattern = "--server-port=";
    if (args.length != 1 || !args[0].startsWith(inputPortPattern)) {
      System.err.println(String.format("Restart client with args %sPORT", inputPortPattern));
      System.exit(-1);
    }
    String address = "127.0.0.1";
    int port = Integer.parseInt(args[0].substring(inputPortPattern.length()).trim());

    try (Client client = new Client(address, port)) {
      client.launch();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
