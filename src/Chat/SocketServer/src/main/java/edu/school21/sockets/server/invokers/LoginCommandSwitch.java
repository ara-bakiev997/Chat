package edu.school21.sockets.server.invokers;

import edu.school21.sockets.exceptions.CommandNotFoundExceptions;
import edu.school21.sockets.server.commands.LoginСommand;
import java.util.Map;
import java.util.HashMap;

public class LoginCommandSwitch {

  private final Map<String, LoginСommand> commands = new HashMap<>();

  public void registerCommand(String commandName, LoginСommand command) {
    commands.put(commandName, command);
  }

  public void execute(String commandName) {
    LoginСommand command = commands.get(commandName);

    if (command == null) {
      throw new CommandNotFoundExceptions("Command not found " + commandName);
    }
    command.execute();
  }

}
