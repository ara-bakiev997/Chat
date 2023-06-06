package edu.school21.sockets.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import edu.school21.sockets.models.User;
import edu.school21.sockets.repositories.UsersRepository;

@Component
public class UsersServiceImpl implements UsersService {
  private final UsersRepository usersRepository;
  private final PasswordEncoder encoder;

  @Autowired
  public UsersServiceImpl(UsersRepository usersRepository, PasswordEncoder encoder) {
    this.usersRepository = usersRepository;
    this.encoder = encoder;
  }

  @Override
  public String signUp(User user) {
    String encodedPassword = encoder.encode(user.getPassword());
    user.setPassword(encodedPassword);

    if (usersRepository.findByName(user.getUserName()).isPresent()) {
      return "User with given name already exists";
    }
    usersRepository.save(user);
    return "Successful!";
  }
}
