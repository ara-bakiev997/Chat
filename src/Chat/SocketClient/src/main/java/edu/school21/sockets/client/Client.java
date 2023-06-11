package edu.school21.sockets.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client implements AutoCloseable {
  private Socket client;
  private Reader reader;
  private Writer writer;

  public Client(String address, int port) throws UnknownHostException, IOException {
    client = new Socket(address, port);
  }

  public void launch() throws UnknownHostException, IOException {
    try {
      reader = new Reader(new BufferedReader(new InputStreamReader(client.getInputStream())));
      writer = new Writer(new BufferedWriter(new OutputStreamWriter(client.getOutputStream())));

      Thread threadReader = new Thread(reader);
      Thread threadWriter = new Thread(writer);

      threadReader.start();
      threadWriter.start();

      threadReader.join();
      threadWriter.join();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void close() throws IOException {
    if (client != null) {
      client.close();
    }
  }
}
