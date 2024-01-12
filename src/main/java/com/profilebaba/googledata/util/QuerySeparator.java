package com.profilebaba.googledata.util;

import com.profilebaba.googledata.config.QueryConfiguration;
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
    StringBuilder[] phrases = new StringBuilder[] {new StringBuilder(), new StringBuilder()};
    int count = 0;
    for (String word : words) {
      String separatorChar = " ";
      if (count < 2
          && (queryConfiguration.getPunctuationsAndDash().contains(word) || queryConfiguration.getPrepositions().contains(word))){
        count++;
        continue;
      }
      if (count == 0){
        separatorChar = "";
      }
      phrases[count].append(separatorChar).append(word);
    }
    String location = phrases[1].toString().trim();
    if (location.isBlank() || location.equals("me")) {
      location = currentLocation;
    }
    SearchQueryPhrase searchQueryPhrase = new SearchQueryPhrase(phrases[0].toString(), location);
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