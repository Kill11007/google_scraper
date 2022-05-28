package com.profilebaba.googledata.dto.deserializer;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.profilebaba.googledata.dto.GoogleResponse;
import com.profilebaba.googledata.dto.Record;
import com.profilebaba.googledata.dto.Record.RecordBuilder;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class RecordDeserializer extends StdDeserializer<GoogleResponse> {

  public RecordDeserializer() {
    this(List.class);
  }

  protected RecordDeserializer(Class<?> vc) {
    super(vc);
  }

  private Record createRecord(JsonNode recordNode) {
    RecordBuilder recordBuilder = Record.builder();

    //Name
    JsonNode nameNode = recordNode.get(11);
    if (nameNode.isValueNode()) {
      recordBuilder.name(nameNode.asText());
    }

    //Address
    JsonNode addressTreeNode = recordNode.get(39);
    if (addressTreeNode.isValueNode()) {
      recordBuilder.address(addressTreeNode.asText());
    }

    //Latitude and Longitude
    /*TreeNode latLngTreeNode = treeNode.get(9);
    if (latLngTreeNode.isArray()) {
      //latitude
      TreeNode latitude = latLngTreeNode.get(2);
      if (latitude.isValueNode()) {
        builder.latitude(latitude.toString());
      }

      //longitude
      TreeNode longitude = latLngTreeNode.get(3);
      if (longitude.isValueNode()) {
        builder.longitude(longitude.toString());
      }

    }*/
    return recordBuilder.build();
  }

  @Override
  public GoogleResponse deserialize(JsonParser jsonParser,
      DeserializationContext deserializationContext)
      throws IOException {
    TreeNode treeNode = jsonParser.readValueAsTree();
    TreeNode zeroNode = treeNode.get(0);
    TreeNode dataNode = zeroNode.get(1);
    List<Record> records = new ArrayList<>();
    if (dataNode.isArray()) {
      ArrayNode arrayDataNode = (ArrayNode) dataNode;
      int dataNodeSize = arrayDataNode.size();
      for (int i = 1; i < dataNodeSize; i++) {

        JsonNode jsonNode = arrayDataNode.get(i);
        if (jsonNode.isArray()) {
          ArrayNode tRecordNode = (ArrayNode) jsonNode;
          JsonNode recordNode = tRecordNode.get(14);
          if (recordNode.isArray()) {
            Record record = createRecord(recordNode);
            records.add(record);
          }
        }
      }
    }
    GoogleResponse googleResponse = new GoogleResponse(records);
    return googleResponse;
  }
}
