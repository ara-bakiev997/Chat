package edu.school21.sockets.models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.Getter;
import lombok.Setter;
import lombok.EqualsAndHashCode;

@Getter
@Setter
@EqualsAndHashCode
public class Message {
  private Long id;
  private User author;
  private ChatRoom room;
  private String text;
  private LocalDateTime dateTime;

  private final static DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("dd/MM/yy HH:mm");

  public Message(Long id, User author, ChatRoom room, String text, LocalDateTime dateTime) {
    this.id = id;
    this.author = author;
    this.room = room;
    this.text = text;
    this.dateTime = dateTime;
  }

  @Override
  public String toString() {
    return "Message : {\nid=" + id + ",\nauthor=" + author + ",\ntext=" + text + ",\ndataTime="
        + FORMAT.format(dateTime) + "\n}";
  }
}
