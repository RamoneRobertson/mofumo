package com.mofumo.api.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class HomeController {
  @GetMapping("/")
  public String home() {
    return "redirect:/index.html";
  }
}
