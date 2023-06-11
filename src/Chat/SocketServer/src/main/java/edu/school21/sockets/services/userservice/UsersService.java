package edu.school21.sockets.services.userservice;

import edu.school21.sockets.models.User;

public interface UsersService {
  String signUp(User user);

  boolean isRegistered(User user);
}
