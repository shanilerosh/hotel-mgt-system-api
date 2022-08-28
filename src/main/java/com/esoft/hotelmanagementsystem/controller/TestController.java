package com.esoft.hotelmanagementsystem.controller;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ShanilErosh
 */
@RestController
public class TestController {

    @GetMapping("/test1")
    @ApiImplicitParams({ @ApiImplicitParam(name = "Authorization", required = true, dataType = "string", paramType = "header") })
    public String hello() {
        return "Received";
    }
}
