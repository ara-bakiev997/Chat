package edu.school21.sockets.services.messageservice;

import java.util.List;
import edu.school21.sockets.models.Message;

public interface MessagesService {
  void saveMessage(Message message);

  List<Message> findAllMessagesInRoom(Long id);
}
