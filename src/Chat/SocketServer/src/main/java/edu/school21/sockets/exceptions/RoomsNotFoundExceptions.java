package edu.school21.sockets.exceptions;

public class RoomsNotFoundExceptions extends RuntimeException {

  public RoomsNotFoundExceptions() {
    super();
  }

  public RoomsNotFoundExceptions(String message) {
    super(message);
  }

  public RoomsNotFoundExceptions(String message, Exception e) {
    super(message, e);
  }
}
