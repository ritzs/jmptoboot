package com.example.jmptoboot.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
public class TestApiController {


    @RequestMapping(value = {"/hello", "/sbb"}, method = RequestMethod.GET)
    public String hello() {
        return "hello World!!!!";
    }


}
