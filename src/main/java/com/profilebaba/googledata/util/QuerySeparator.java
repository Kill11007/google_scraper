package com.profilebaba.googledata.util;

import com.profilebaba.googledata.config.QueryConfiguration;
import java.util.Arrays;
import java.util.stream.Collectors;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Log4j2
public class QuerySeparator {

  private final QueryConfiguration queryConfiguration;

  public SearchQueryPhrase getPhrase(String phrase, String currentLocation){
    String[] words = phrase.split("\\s+");
    String[] phrases = new String[]{"", ""};
    int count = 0;
    StringBuilder temp = new StringBuilder();
    for (String word : words) {
      log.info("Word: {}", word);
      if (count == 0 && (queryConfiguration.getPunctuationsAndDash().contains(word)
          || queryConfiguration.getPrepositions().contains(word))){
        phrases[count++] = temp.toString();
        temp = new StringBuilder();
        continue;
      }
      temp.append(" ").append(word);
      phrases[count] += " " + word;
    }
    log.info("Count: {}, Phrase 1: {}", count, phrases[0]);
    log.info("Count: {}, Phrase 2: {}", count, phrases[1]);

    if (words.length < 2){
      return new SearchQueryPhrase(phrases[0], currentLocation);
    }
    String location = phrases[1];
    if (location.isBlank() || location.equalsIgnoreCase("me")) {
      location = currentLocation;
    }
    SearchQueryPhrase searchQueryPhrase = new SearchQueryPhrase(phrases[0], location);
    log.info("SearchPhrase: {}", searchQueryPhrase);
    return searchQueryPhrase;
  }

  @Data
  @NoArgsConstructor
  public static class SearchQueryPhrase{

    private String query;
    private String location;

    public SearchQueryPhrase(String query, String location) {
      setQuery(query);
      this.location = location;
    }

    public void setQuery(String query){
      this.query = query;
    }
  }

}