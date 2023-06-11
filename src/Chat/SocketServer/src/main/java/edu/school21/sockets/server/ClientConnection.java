package edu.school21.sockets.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.IOException;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import edu.school21.sockets.exceptions.CommandNotFoundExceptions;
import edu.school21.sockets.exceptions.RoomsNotFoundExceptions;
import edu.school21.sockets.models.ChatRoom;
import edu.school21.sockets.models.Message;
import edu.school21.sockets.models.User;
import edu.school21.sockets.server.receivers.LoginReceiver;
import edu.school21.sockets.server.invokers.LoginCommandSwitch;
import edu.school21.sockets.server.receivers.ManipulateRoomsReceiver;
import edu.school21.sockets.server.invokers.ManipulateRoomsCommandSwitch;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@EqualsAndHashCode
@RequiredArgsConstructor
public class ClientConnection implements Runnable {
  @NonNull
  private Socket clientSocket;

  @Getter
  @NonNull
  private Server server;

  @Getter
  @Setter
  private User user = new User();

  @Getter
  @Setter
  private ChatRoom room = new ChatRoom();

  private BufferedWriter writer;
  private BufferedReader reader;

  private LoginReceiver loginReceiver = new LoginReceiver(this);
  private LoginCommandSwitch loginCommandSwitch = new LoginCommandSwitch();

  {
    loginCommandSwitch.registerCommand("1", loginReceiver::signIn);
    loginCommandSwitch.registerCommand("2", loginReceiver::signUp);
    loginCommandSwitch.registerCommand("3", loginReceiver::exit);
  }

  private ManipulateRoomsReceiver manipulateRoomsReceiver = new ManipulateRoomsReceiver(this);
  private ManipulateRoomsCommandSwitch manipulateRoomsCommandSwitch =
      new ManipulateRoomsCommandSwitch();

  {
    manipulateRoomsCommandSwitch.registerCommand("1", manipulateRoomsReceiver::createRoom);
    manipulateRoomsCommandSwitch.registerCommand("2", manipulateRoomsReceiver::chooseRoom);
    manipulateRoomsCommandSwitch.registerCommand("3", manipulateRoomsReceiver::exit);
  }


  @Override
  public void run() {
    System.out.println("Client accepted " + clientSocket.getInetAddress() + " "
        + clientSocket.getPort() + " " + clientSocket.getLocalPort());
    try (
        BufferedWriter writer =
            new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
        BufferedReader reader =
            new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {

      this.writer = writer;
      this.reader = reader;

      logIn();

      if (!Thread.currentThread().isInterrupted()) {
        System.out.println("Registration completed successfully " + clientSocket.getInetAddress()
            + " " + clientSocket.getPort() + " " + clientSocket.getLocalPort());
        manipulateRooms();
      }

      if (!Thread.currentThread().isInterrupted()) {
        System.out.println("Room selected " + room);
        sendLatestMessages();
        communicate();
      }

    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      disconnect();
    }
  }

  public String readInputStream() {
    try {
      return reader.readLine();
    } catch (IOException e) {
      return null; //TODO mb disconnect
    }
  }

  public void writeOutputStream(String message) {
    try {
      writer.write(message + "\n");
      writer.flush();
    } catch (IOException e) {
      disconnect();
    }
  }

  public void signUpUser() {
    server.signUpUser(this, user);
  }

  public void signInUser() {
    server.signInUser(this, user);
  }

  public void createRoom(ChatRoom room) {
    room.setCreator(user);
    server.createRoom(room);
  }

  public List<ChatRoom> getAllRooms() {
    return server.getAllRooms();
  }

  public void chooseRoom(Long id) {
    room = server.getRoomById(id).get();
    System.out.println(room);
  }

  public void disconnect() {
    System.out.println("Client disconnected " + clientSocket.getInetAddress() + " "
        + clientSocket.getPort() + " " + clientSocket.getLocalPort());
    try {
      if (writer != null) {
        writer.close();
      }
      if (reader != null) {
        reader.close();
      }
      if (clientSocket != null) {
        clientSocket.close();
      }
      server.disconnectConnection(this);
      Thread.currentThread().interrupt();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void logIn() {
    writeOutputStream("Hello from Server!\n" + "1. signIn\n" + "2. signUp\n" + "3. Exit");
    while (!Thread.currentThread().isInterrupted()) {
      String request = readInputStream();

      if (request != null) {
        request = request.trim().toUpperCase();

        try {
          loginCommandSwitch.execute(request);
          break;
        } catch (Exception e) {
          writeOutputStream(
              "Command not recognized, enter the following command:\n1. signIn\n2. signUp\n3. Exit");
        }

      } else {
        disconnect();
        break;
      }
    }
  }

  private void manipulateRooms() {
    writeOutputStream("Rooms:\n" + "1. Create room\n" + "2. Choose room\n" + "3. Exit");

    while (!Thread.currentThread().isInterrupted()) {
      String request = readInputStream();

      if (request != null) {
        request = request.trim().toUpperCase();

        try {
          manipulateRoomsCommandSwitch.execute(request);
          if ("1".equalsIgnoreCase(request)) {
            writeOutputStream("Rooms:\n" + "1. Create room\n" + "2. Choose room\n" + "3. Exit");
          } else {
            break;
          }

        } catch (CommandNotFoundExceptions e) {
          writeOutputStream(
              "Command not recognized, enter the following command:\n1. Create room\n2. Choose room\n3. Exit");

        } catch (RoomsNotFoundExceptions e) {
          writeOutputStream(
              "No rooms created\nCommand not recognized, enter the following command:\n1. Create room\n2. Choose room\n3. Exit");

        } catch (Exception e) {
          e.printStackTrace();
        }

      } else {
        disconnect();
        break;
      }
    }
  }

  private void sendLatestMessages() {
    StringBuilder latestMessagesFromRoom = new StringBuilder(room.getName());

    List<Message> allMessages = server.getAllMessagesInRoom(room.getId());

    if (allMessages.isEmpty()) {
      latestMessagesFromRoom.append(" ---");
    } else {
      latestMessagesFromRoom.append("\n")
          .append(allMessages.stream()
              .map(message -> message.getAuthor().getUserName() + ": " + message.getText())
              .collect(Collectors.joining("\n")));
    }
    writeOutputStream(latestMessagesFromRoom.toString());
  }


  private void communicate() {
    String response = "Start messaging";
    writeOutputStream(response);
    while (!Thread.currentThread().isInterrupted()) {
      String textMessage = readInputStream();
      if (textMessage != null) {
        if ("exit".equalsIgnoreCase(textMessage.trim())) {
          writeOutputStream("You have left the chat.");
          disconnect();
          break;
        } else {
          server.sendBroadcastMessage(
              new Message(null, user, room, textMessage, LocalDateTime.now()));
        }
      } else {
        disconnect();
        break;
      }
    }
  }

}

