package edu.school21.sockets.repositories.roomsrepository;

import java.util.Optional;
import edu.school21.sockets.models.ChatRoom;
import edu.school21.sockets.repositories.CrudRepository;

public interface RoomsRepository extends CrudRepository<ChatRoom> {
  Optional<ChatRoom> findByName(String name);
}
