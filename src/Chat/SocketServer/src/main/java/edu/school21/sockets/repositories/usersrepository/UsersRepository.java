package edu.school21.sockets.repositories.usersrepository;

import java.util.Optional;
import edu.school21.sockets.models.User;
import edu.school21.sockets.repositories.CrudRepository;

public interface UsersRepository extends CrudRepository<User> {
  Optional<User> findByName(String name);
}
