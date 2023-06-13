package edu.school21.sockets.exceptions;

public class CommandNotFoundExceptions extends RuntimeException {

  public CommandNotFoundExceptions() {
    super();
  }

  public CommandNotFoundExceptions(String message) {
    super(message);
  }

  public CommandNotFoundExceptions(String message, Exception e) {
    super(message, e);
  }
}
