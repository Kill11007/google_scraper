package com.profilebaba.googledata.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("*")
@RequestMapping("/")
public class DefaultController {
  @GetMapping
  public ResponseEntity<?> get() {
    return ResponseEntity.ok().build();
  }
}
