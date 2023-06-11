package edu.school21.sockets.server.invokers;

import java.util.HashMap;
import java.util.Map;
import edu.school21.sockets.exceptions.CommandNotFoundExceptions;
import edu.school21.sockets.server.commands.ManipulateRoomsCommand;

public class ManipulateRoomsCommandSwitch {
  private final Map<String, ManipulateRoomsCommand> commands = new HashMap<>();

  public void registerCommand(String commandName, ManipulateRoomsCommand command) {
    commands.put(commandName, command);
  }

  public void execute(String commandName) {
    ManipulateRoomsCommand command = commands.get(commandName);

    if (command == null) {
      throw new CommandNotFoundExceptions("Command not found " + commandName);
    }
    command.execute();
  }
}
