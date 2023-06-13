package edu.school21.sockets.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Set;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import edu.school21.sockets.config.SocketsApplicationConfig;
import edu.school21.sockets.models.ChatRoom;
import edu.school21.sockets.models.Message;
import edu.school21.sockets.models.User;
import edu.school21.sockets.repositories.utils.TableInitializer;
import edu.school21.sockets.services.messageservice.MessagesService;
import edu.school21.sockets.services.roomservice.RoomsService;
import edu.school21.sockets.services.userservice.UsersService;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


public class Server implements AutoCloseable {

  private ApplicationContext contex;
  private UsersService usersService;
  private MessagesService messagesService;
  private RoomsService roomsService;
  private ServerSocket serverSocket;
  private Set<ClientConnection> allConnections = new LinkedHashSet<>();

  public Server(int port) throws IOException, BeansException, SQLException {
    serverSocket = new ServerSocket(port);
    contex = new AnnotationConfigApplicationContext(SocketsApplicationConfig.class);

    contex.getBean(TableInitializer.class).initializeTablesWithData();

    usersService = contex.getBean(UsersService.class);
    messagesService = contex.getBean(MessagesService.class);
    roomsService = contex.getBean(RoomsService.class);
  }

  public void launch() throws IOException {
    System.out.println("Server start...");

    while (true) {
      Socket clientSocket = serverSocket.accept();
      ClientConnection clientThread = new ClientConnection(clientSocket, this);
      new Thread(clientThread).start();
    }
  }

  public synchronized void signUpUser(ClientConnection connection, User user) {
    String response = usersService.signUp(user);
    connection.writeOutputStream(response);
    if ("User with given name already exists".equalsIgnoreCase(response)) {
      connection.disconnect();
    } else if ("Successful!".equalsIgnoreCase(response)) {
      allConnections.add(connection);
    }
  }

  public synchronized void signInUser(ClientConnection connection, User user) {
    if (usersService.isRegistered(user)) {
      connection.writeOutputStream("Authorization was successful");
      allConnections.add(connection);
    } else {
      connection.writeOutputStream("Authorization was not successful");
      connection.disconnect();
    }
  }

  public synchronized void createRoom(ChatRoom room) {
    roomsService.saveRoom(room);
  }

  public synchronized List<ChatRoom> getAllRooms() {
    return roomsService.getAllRooms();
  }

  public synchronized Optional<ChatRoom> getRoomById(Long id) {
    return roomsService.findById(id);
  }

  public synchronized List<Message> getAllMessagesInRoom(Long id) {
    return messagesService.findAllMessagesInRoom(id);
  }

  public synchronized void sendBroadcastMessage(Message message) {
    messagesService.saveMessage(message);
    for (ClientConnection connection : allConnections) {
      if (connection.getRoom().equals(message.getRoom())) {
        connection.writeOutputStream(message.getAuthor().getUserName() + ": " + message.getText());
      }
    }
  }

  public synchronized void disconnectConnection(ClientConnection connetion) {
    allConnections.remove(connetion);
  }

  @Override
  public void close() throws IOException {
    if (serverSocket != null) {
      serverSocket.close();
    }
  }

}
