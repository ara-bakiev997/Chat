package edu.school21.sockets.services.roomservice;

import java.util.List;
import java.util.Optional;
import edu.school21.sockets.models.ChatRoom;

public interface RoomsService {
  void saveRoom(ChatRoom room);

  List<ChatRoom> getAllRooms();

  Optional<ChatRoom> findById(Long id);
}
