package com.qingfengzhuyue.forum.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {

    @CrossOrigin
    @RequestMapping("/")
    public String index(){
        return "index";
    }
}
