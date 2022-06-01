package com.profilebaba.googledata.config;

import com.profilebaba.googledata.entity.Category;
import com.profilebaba.googledata.entity.Location;
import com.profilebaba.googledata.repository.CategoryRepository;
import com.profilebaba.googledata.repository.LocationRepository;
import com.profilebaba.googledata.repository.ResponseRepository;
import java.util.HashMap;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfiguration {

  private final CategoryRepository categoryRepository;

  @Bean
  public HashMap<String, String> queryParams() {
    HashMap<String, String> params = new HashMap<>();
    params.put("tbm", "map");
    params.put("authuser", "0");
    params.put("hl", "en");
    params.put("gl", "in");
    params.put("pb",
        "!4m12!1m3!1d69770.46649053734!2d77.1969619!3d28.6824774!2m3!1f0!2f0!3f0!3m2!1i1920!2i478!4f13.1!7i{0}!8i{1}!10b1!12m8!1m1!18b1!2m3!5m1!6e2!20e3!10b1!16b1!19m4!2m3!1i360!2i120!4i8!20m57!2m2!1i203!2i100!3m2!2i4!5b1!6m6!1m2!1i86!2i86!1m2!1i408!2i240!7m42!1m3!1e1!2b0!3e3!1m3!1e2!2b1!3e2!1m3!1e2!2b0!3e3!1m3!1e8!2b0!3e3!1m3!1e10!2b0!3e3!1m3!1e10!2b1!3e2!1m3!1e9!2b1!3e2!1m3!1e10!2b0!3e3!1m3!1e10!2b1!3e2!1m3!1e10!2b0!3e4!2b1!4b1!9b0!22m3!1s_4CEYsz6H5idseMP2tm5sAM!2s1i%3A2%2Ct%3A12696%2Ce%3A1%2Cp%3A_4CEYsz6H5idseMP2tm5sAM%3A968!7e81!24m65!1m21!13m8!2b1!3b1!4b1!6i1!8b1!9b1!14b1!20b1!18m11!3b1!4b1!5b1!6b1!9b1!12b1!13b1!14b1!15b1!17b1!20b1!2b1!5m5!2b1!3b1!5b1!6b1!7b1!10m1!8e3!14m1!3b1!17b1!20m2!1e3!1e6!24b1!25b1!26b1!29b1!30m1!2b1!36b1!43b1!52b1!54m1!1b1!55b1!56m2!1b1!3b1!65m5!3m4!1m3!1m2!1i224!2i298!71b1!72m4!1m2!3b1!5b1!4b1!89b1!26m4!2m3!1i80!2i92!4i8!30m28!1m6!1m2!1i0!2i0!2m2!1i458!2i478!1m6!1m2!1i1870!2i0!2m2!1i1920!2i478!1m6!1m2!1i0!2i0!2m2!1i1920!2i20!1m6!1m2!1i0!2i458!2m2!1i1920!2i478!34m17!2b1!3b1!4b1!6b1!8m5!1b1!3b1!4b1!5b1!6b1!9b1!12b1!14b1!20b1!23b1!25b1!26b1!37m1!1e81!42b1!46m1!1e14!47m0!49m5!3b1!6m1!1b1!7m1!1e3!50m33!1m28!2m7!1u3!4sOpen+now!5e1!9s0ahUKEwid-qj2_ur3AhU_UGwGHQpdAfMQ_KkBCLwFKBY!10m2!3m1!1e1!2m7!1u2!4sTop-rated!5e1!9s0ahUKEwid-qj2_ur3AhU_UGwGHQpdAfMQ_KkBCL0FKBc!10m2!2m1!1e1!3m1!1u3!3m1!1u2!3m6!1u17!2m4!1m2!17m1!1e2!2sDistance!4BIAE!2e2!3m2!1b1!3b1!59BQ2dBd0Fn!67m2!7b1!10b1!69i603");
    params.put("q", "{0}");
    return params;
  }

  @Bean
  public RestTemplate restTemplate() {
    RestTemplate restTemplate = new RestTemplate();
    return restTemplate;
  }

  @Bean(name = "all-categories")
  @ConditionalOnProperty(prefix = "categories", name = "all", havingValue = "true")
  public List<Category> categories() {
    return categoryRepository.findAll();
  }
}
