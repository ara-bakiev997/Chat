package edu.school21.sockets.server.receivers;

import edu.school21.sockets.server.ClientConnection;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LoginReceiver {
  private final ClientConnection connection;

  public void signIn() {
    initUser();
    connection.signInUser();
  }

  public void signUp() {
    initUser();
    connection.signUpUser();
  }

  public void exit() {
    connection.writeOutputStream("You have left the chat.");
    connection.disconnect();
  }

  private void initUser() {
    String response = "Enter username:";
    connection.writeOutputStream(response);

    String request = connection.readInputStream();
    connection.getUser().setUserName(request);

    response = "Enter password:";
    connection.writeOutputStream(response);
    request = connection.readInputStream();
    connection.getUser().setPassword(request);
  }

}
