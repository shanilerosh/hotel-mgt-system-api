package com.esoft.hotelmanagementsystem.controller;

import com.esoft.hotelmanagementsystem.config.EmailUtil;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ShanilErosh
 */
@RestController
@RequiredArgsConstructor
public class TestController {

    private final RabbitTemplate rabbitTemplate;
    private final EmailUtil emailUtil;

    @GetMapping("/test1")
    @ApiImplicitParams({ @ApiImplicitParam(name = "Authorization", required = true, dataType = "string", paramType = "header") })
    public String hello() {
        return "hello";
    }
}
