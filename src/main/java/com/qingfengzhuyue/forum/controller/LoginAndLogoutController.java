package com.qingfengzhuyue.forum.controller;

import com.qingfengzhuyue.forum.mapper.UserMapper;
import com.qingfengzhuyue.forum.model.User;
import com.qingfengzhuyue.forum.model.UserExample;
import com.qingfengzhuyue.forum.result.CommonResult;
import com.qingfengzhuyue.forum.service.LoginService;
import com.qingfengzhuyue.forum.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.UUID;

@Controller
public class LoginAndLogoutController {

    @Autowired
    private LoginService loginService;
    @Autowired
    private UserService userService;

    @CrossOrigin
    @RequestMapping(value = "/api/login", method = RequestMethod.POST,  produces = "application/json; charset=UTF-8")
    @ResponseBody
    public CommonResult login(@RequestBody User user, HttpServletResponse response) {

        System.out.println(user.getName());
        List<User> userList = loginService.login(user);
        if (userList.size()!=0){
            System.out.println("成功");
            String token = UUID.randomUUID().toString();
            user.setToken(token);
            userService.createOrUpdate(user);

            response.addCookie(new Cookie("token",token));

        }
        List<User> users = loginService.login(user);
        return CommonResult.success(users);
    }
}
