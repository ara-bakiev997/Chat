package edu.school21.sockets.services.jsonservice;

import edu.school21.sockets.models.JsonObject;

public interface JsonService {
  JsonObject parseJsonString(String jsonString);

  String createJsonString(JsonObject jsonObject);
}
