package edu.school21.sockets.services.roomservice;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import edu.school21.sockets.models.ChatRoom;
import edu.school21.sockets.repositories.roomsrepository.RoomsRepository;

@Component
public class RoomsServiceImpl implements RoomsService {
  private final RoomsRepository roomsRepository;

  @Autowired
  public RoomsServiceImpl(RoomsRepository roomsRepository) {
    this.roomsRepository = roomsRepository;
  }

  @Override
  public void saveRoom(ChatRoom room) {
    roomsRepository.save(room);
  }

  @Override
  public List<ChatRoom> getAllRooms() {
    return roomsRepository.findAll();
  }

  @Override
  public Optional<ChatRoom> findById(Long id) {
    return roomsRepository.findById(id);
  }
}
