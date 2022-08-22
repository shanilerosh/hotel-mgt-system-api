package com.esoft.hotelmanagementsystem.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ShanilErosh
 */
@RestController
public class TestController {

    @GetMapping
    public String hello() {
        return "Received";
    }
}
