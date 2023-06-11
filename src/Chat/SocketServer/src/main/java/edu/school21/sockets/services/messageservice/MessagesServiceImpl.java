package edu.school21.sockets.services.messageservice;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import edu.school21.sockets.models.Message;
import edu.school21.sockets.repositories.messagesrepository.MessagesRepository;

@Component
public class MessagesServiceImpl implements MessagesService {
  private final MessagesRepository messagesRepository;

  @Autowired
  public MessagesServiceImpl(MessagesRepository messagesRepository) {
    this.messagesRepository = messagesRepository;
  }

  @Override
  public void saveMessage(Message message) {
    messagesRepository.save(message);
  }

  @Override
  public List<Message> findAllMessagesInRoom(Long id) {
    return messagesRepository.findAllMessagesInRoom(id);
  }

}
