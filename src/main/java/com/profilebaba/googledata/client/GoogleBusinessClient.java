package com.profilebaba.googledata.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.profilebaba.googledata.dto.GoogleResponse;
import com.profilebaba.googledata.dto.Request;
import java.net.URI;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@RequiredArgsConstructor
@Log4j2
@Component
public class GoogleBusinessClient {

  @Value("${google.url:https://www.google.com/search}")
  private String googleURL;

  private final HashMap<String, String> queryParams;

  private final RestTemplate restTemplate;

  private final ObjectMapper objectMapper;

  private int userAgentCounter;

  public Optional<GoogleResponse> search(Request request) throws JsonProcessingException {
    MultiValueMap<String, String> newQueryParamsMap = getQueryParams(request);
    URI uri = UriComponentsBuilder.fromUriString(googleURL).queryParams(newQueryParamsMap).build()
        .toUri();
    log.info("URI: {}", uri.toString());
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.setContentType(MediaType.APPLICATION_JSON);
    httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    httpHeaders.set("User-Agent", getUserAgent());
    HttpEntity httpEntity = new HttpEntity(httpHeaders);
    ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, httpEntity,
        String.class);
    Optional<GoogleResponse> googleResponse = Optional.empty();
    if (response.getStatusCode() == HttpStatus.OK) {
      String body = response.getBody();
      body = body.replace(")]}'", "");
//      log.info(body);
      googleResponse = Optional.ofNullable(
          objectMapper.readValue(body, GoogleResponse.class));
    }
    return googleResponse;
  }

  private MultiValueMap<String, String> getQueryParams(Request request) {
    MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
    for (Map.Entry<String, String> entry : queryParams.entrySet()) {
      String key = entry.getKey();
      String value = entry.getValue();
      if (entry.getKey().equals("pb")) {
        String pb = queryParams.get("pb");
        value = MessageFormat.format(pb, request.getTotalRecords(), request.getOffset());
      } else if (entry.getKey().equals("q")) {
        String q = queryParams.get("q");
        value = MessageFormat.format(q, request.getQuery());
      }
      multiValueMap.add(key, value);
    }
    return multiValueMap;
  }

  private String getUserAgent() {
    List<String> userAgents = List.of(
        "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/101.0.4951.64 Safari/537.36 Edg/101.0.1210.53",
        "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/101.0.4951.67 Safari/537.36",
        "Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; .NET4.0C; .NET4.0E; .NET CLR 2.0.50727; .NET CLR 3.0.30729; .NET CLR 3.5.30729; wbx 1.0.0; wbxapp 1.0.0; rv:11.0) like Gecko",
        "PostmanRuntime/7.29.0");

    return userAgents.get(userAgentCounter >= userAgents.size() ? 0 : userAgentCounter++);
  }
}
