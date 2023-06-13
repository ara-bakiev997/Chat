package edu.school21.sockets.repositories.messagesrepository;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import java.sql.Timestamp;
import edu.school21.sockets.models.Message;
import edu.school21.sockets.models.User;
import edu.school21.sockets.repositories.usersrepository.UsersRepository;

@Component
public class MessagesRepositoryJdbcTemplateImpl implements MessagesRepository {
  private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
  private final UsersRepository usersRepository;

  public MessagesRepositoryJdbcTemplateImpl(
      @Qualifier("messagesRepositoryNamedParameter") NamedParameterJdbcTemplate namedParameterJdbcTemplate,
      UsersRepository usersRepository) {
    this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    this.usersRepository = usersRepository;
  }

  @Override
  public Optional<Message> findById(Long id) {
    String queryForFindById = "select * from messages where id = :id";

    return namedParameterJdbcTemplate.query(queryForFindById,
        new MapSqlParameterSource().addValue("id", id), (rs) -> {
          if (rs.next()) {
            return Optional.of(
                new Message(rs.getLong("id"), usersRepository.findById(rs.getLong("author")).get(),
                    null, rs.getString("text"), rs.getTimestamp("dateTime").toLocalDateTime()));
          }
          return Optional.empty();
        });
  }

  @Override
  public void save(Message message) {
    if (message.getAuthor() == null) {
      throw new RuntimeException("Author or room is null");
    }
    Optional<User> optionalUser = usersRepository.findById(message.getAuthor().getId());

    if (optionalUser.isEmpty()) {
      throw new RuntimeException("Author or room is not in Data Base");
    }

    String insertQuery =
        "insert into messages (author, room, text, dateTime) values (:author, :room, :text, :dateTime);";

    namedParameterJdbcTemplate.update(insertQuery,
        new MapSqlParameterSource().addValue("author", message.getAuthor().getId())
            .addValue("room", message.getRoom().getId()).addValue("text", message.getText())
            .addValue("dateTime", Timestamp.valueOf(message.getDateTime())));
  }

  @Override
  public void update(Message message) {
    String updateQuery =
        "update messages set author = :author, room = :room, text = :text, dateTime = :dateTime WHERE Messages.id = :id";

    namedParameterJdbcTemplate.update(updateQuery,
        new MapSqlParameterSource().addValue("author", message.getAuthor().getId())
            .addValue("room", message.getRoom().getId()).addValue("text", message.getText())
            .addValue("dateTime", Timestamp.valueOf(message.getDateTime()))
            .addValue("id", message.getId()));
  }

  @Override
  public List<Message> findAllMessagesInRoom(Long id) {
    String queryForFindAllMessagesInRoom = "select * from messages where room = :room limit 30;";
    MapSqlParameterSource params = new MapSqlParameterSource();
    params.addValue("room", id);

    return namedParameterJdbcTemplate.query(queryForFindAllMessagesInRoom, params, (rs, rowNum) -> {
      return new Message(rs.getLong("id"), usersRepository.findById(rs.getLong("author")).get(),
          null, rs.getString("text"), rs.getTimestamp("dateTime").toLocalDateTime());
    });
  }
}


