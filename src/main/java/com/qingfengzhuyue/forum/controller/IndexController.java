package com.qingfengzhuyue.forum.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    @CrossOrigin
    @GetMapping("/")
    public String index(){
        return "index";
    }
}
