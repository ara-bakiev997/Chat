package edu.school21.sockets.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import edu.school21.sockets.config.SocketsApplicationConfig;
import edu.school21.sockets.models.User;
import edu.school21.sockets.repositories.utils.TableInitializer;
import edu.school21.sockets.services.UsersService;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


public class Server implements AutoCloseable {

  private ApplicationContext contex;
  private UsersService usersService;
  private ServerSocket serverSocket;
  private BufferedWriter writer;
  private BufferedReader reader;

  public Server(int port) throws IOException, BeansException, SQLException {
    serverSocket = new ServerSocket(port);
    contex = new AnnotationConfigApplicationContext(SocketsApplicationConfig.class);
    contex.getBean(TableInitializer.class).initializeTablesWithData();
    usersService = contex.getBean(UsersService.class);
  }

  public void launch() throws IOException {
    System.out.println("Server start");

    while (true) {
      Socket client = serverSocket.accept();
      ClientThread clientThread = new ClientThread(client);
      new Thread(clientThread).start();
    }


    // try (Socket client = serverSocket.accept()) {
    //
    // System.out.println("Client accepted " + client.getInetAddress() + " " + client.getPort() + "
    // "
    // + client.getLocalPort());
    //
    // writer = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
    // reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
    //
    // String userName = "";
    // String password = "";
    //
    // String response = "Hello from Server!";
    // writeOutputStream(response);
    //
    // while (true) {
    // String request = readInputStream();
    // System.out.println(request);
    //
    // if ("signup".equalsIgnoreCase(request.trim())) {
    // response = "Enter username:";
    // writeOutputStream(response);
    // userName = readInputStream();
    // System.out.println("userName = " + userName);
    //
    // response = "Enter password:";
    // writeOutputStream(response);
    // password = readInputStream();
    // System.out.println("password = " + password);
    //
    // response = usersService.signUp(new User(null, userName, password));
    // writeOutputStream(response);
    //
    // break;
    // } else {
    // response = "Registration failed enter: \"signup\"";
    // writeOutputStream(response);
    // }
    // }
    // }
  }

  private String readInputStream() throws IOException {
    return reader.readLine();
  }

  private void writeOutputStream(String message) throws IOException {
    writer.write(message + "\n");
    writer.flush();
  }

  @Override
  public void close() throws IOException {
    if (writer != null) {
      writer.close();
    }
    if (reader != null) {
      reader.close();
    }
    if (serverSocket != null) {
      serverSocket.close();
    }
  }
}
