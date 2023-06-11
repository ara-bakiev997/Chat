package edu.school21.sockets.repositories.messagesrepository;

import java.util.List;
import java.util.Optional;
import edu.school21.sockets.models.Message;

public interface MessagesRepository {
  Optional<Message> findById(Long id);

  void save(Message message);

  void update(Message message);
  
  List<Message> findAllMessagesInRoom(Long id);
}
