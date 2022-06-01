package com.profilebaba.googledata.dto.deserializer;

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
    if (nameNode != null && nameNode.isValueNode()) {
      recordBuilder.name(nameNode.asText());
    }

    //Address
    JsonNode addressTreeNode = recordNode.get(39);
    if (addressTreeNode != null && addressTreeNode.isValueNode()) {
      recordBuilder.address(addressTreeNode.asText());
    }

    //Latitude and Longitude
    JsonNode latLong = recordNode.get(9);
    if (latLong != null && latLong.isArray()) {
      //latitude
      JsonNode latitude = latLong.get(2);
      if (latitude != null && latitude.isValueNode()) {
        recordBuilder.latitude(latitude.toString());
      }

      //longitude
      JsonNode longitude = latLong.get(3);
      if (longitude != null && longitude.isValueNode()) {
        recordBuilder.longitude(longitude.toString());
      }

    }

    //Google Category Name
    JsonNode categoryNode = recordNode.get(13);
    if (categoryNode != null && !categoryNode.isNull() && categoryNode.isArray()) {
      JsonNode node = categoryNode.get(0);
      if (!node.isNull() && node.isValueNode()) {
        recordBuilder.googleCategory(node.asText());
      }
    }

    //Phone Number
    JsonNode phoneNode = recordNode.get(178);
    if (phoneNode != null && !phoneNode.isNull() && phoneNode.isArray()) {
      JsonNode node1 = phoneNode.get(0);
      if (node1 != null && !node1.isNull() && node1.isArray()) {
        JsonNode node = node1.get(3);
        if (node != null && !node.isNull() && node.isValueNode()) {
          recordBuilder.phone(node.asText());
        }
      }
    }

    recordBuilder.jsonResponse(recordNode.toString());

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
    if (dataNode != null && dataNode.isArray()) {
      ArrayNode arrayDataNode = (ArrayNode) dataNode;
      int dataNodeSize = arrayDataNode.size();
      for (int i = 1; i < dataNodeSize; i++) {
        JsonNode jsonNode = arrayDataNode.get(i);
        if (jsonNode != null && !jsonNode.isNull() && jsonNode.isArray()) {
          ArrayNode tRecordNode = (ArrayNode) jsonNode;
          JsonNode recordNode = tRecordNode.get(14);
          if (recordNode != null && !recordNode.isNull() && recordNode.isArray()) {
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
