package edu.school21.sockets.services.jsonservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.school21.sockets.models.JsonObject;

public class JsonServiceImpl implements JsonService {

  @Override
  public JsonObject parseJsonString(String jsonString) {
    ObjectMapper mapper = new ObjectMapper();
    JsonObject jsonObject = null;

    try {
      jsonObject = mapper.readValue(jsonString, JsonObject.class);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
    return jsonObject;
  }

  @Override
  public String createJsonString(JsonObject jsonObject) {
    ObjectMapper mapper = new ObjectMapper();
    String jsonString = null;

    try {
      jsonString = mapper.writeValueAsString(jsonObject);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
    return jsonString;
  }

}
