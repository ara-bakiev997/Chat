package edu.school21.sockets.server.receivers;

import java.util.List;
import java.util.stream.Collectors;
import edu.school21.sockets.exceptions.RoomsNotFoundExceptions;
import edu.school21.sockets.models.ChatRoom;
import edu.school21.sockets.server.ClientConnection;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ManipulateRoomsReceiver {
  private final ClientConnection connection;

  public void createRoom() {
    ChatRoom room = new ChatRoom();

    String response = "Enter room name:";
    connection.writeOutputStream(response);

    String request = connection.readInputStream();
    room.setName(request);

    connection.createRoom(room);

    connection.writeOutputStream(request + " created");
  }

  public void chooseRoom() {
    List<ChatRoom> allRooms = connection.getAllRooms();

    if (allRooms.isEmpty()) {
      throw new RoomsNotFoundExceptions();
    }

    StringBuilder response = new StringBuilder("Rooms:\n");

    response.append(allRooms.stream().map(room -> room.getId() + ". " + room.getName())
        .collect(Collectors.joining("\n")));

    response.append("\n").append((allRooms.size() + 1)).append(". Exit");

    connection.writeOutputStream(response.toString());

    int id = chooseCommunicationRoom(allRooms.size(), response.toString());

    connection.chooseRoom((long) id);
  }

  public void exit() {
    connection.writeOutputStream("You have left the chat.");
    connection.disconnect();
  }

  private int chooseCommunicationRoom(int count, String response) {
    while (!Thread.currentThread().isInterrupted()) {
      String choose = connection.readInputStream();
      if (choose != null) {
        choose = choose.trim().toLowerCase();
        try {
          int id = Integer.parseInt(choose);
          if (id == count + 1) {
            exit();
          } else if (id < 1) {
            connection.writeOutputStream(
                "Command not recognized, enter the following command:\n" + response);
          } else {
            return id;
          }
        } catch (Exception e) {
          connection.writeOutputStream(
              "Command not recognized, enter the following command:\n" + response);
        }
      } else {
        connection.disconnect();
        break;
      }
    }
    return 0;
  }



}
