package com.esoft.hotelmanagementsystem.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ShanilErosh
 */
@RestController
public class TestController {

    @GetMapping("/test1")
    public String hello() {
        return "Received";
    }
}
