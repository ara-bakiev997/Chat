package edu.school21.sockets.app;

import java.io.IOException;
import java.sql.SQLException;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import edu.school21.sockets.config.SocketsApplicationConfig;
import edu.school21.sockets.repositories.utils.TableInitializer;
import edu.school21.sockets.server.Server;

public class Main {
  public static void main(String[] args) {
    String inputPortPattern = "--port=";
    if (args.length != 1 || !args[0].startsWith(inputPortPattern)) {
      System.err.println(String.format("Restart server with args %sPORT", inputPortPattern));
      System.exit(-1);
    }

    int port = Integer.parseInt(args[0].substring(inputPortPattern.length()).trim());

    try (Server server = new Server(port)) {
      server.launch();
    } catch (IOException | BeansException | SQLException e) {
      e.printStackTrace();
    }
  }
}
