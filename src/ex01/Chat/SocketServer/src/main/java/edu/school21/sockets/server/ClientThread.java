package edu.school21.sockets.server;

import java.net.Socket;

public class ClientThread implements Runnable {
  private Socket clientSocket;

  public ClientThread(Socket clientSocket) {
    this.clientSocket = clientSocket;
  }


  @Override
  public void run() {
    System.out.println("Hello into Thread");

  }
}
