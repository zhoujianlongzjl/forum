package com.qingfengzhuyue.forum.controller;

import com.qingfengzhuyue.forum.model.User;
import com.qingfengzhuyue.forum.result.CommonResult;
import com.qingfengzhuyue.forum.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.UUID;

@Controller
public class LoginAndLogonController {

    @Autowired
    private UserService userService;

    @CrossOrigin
    @RequestMapping(value = "/api/login", method = RequestMethod.POST,  produces = "application/json; charset=UTF-8")
    @ResponseBody
    public CommonResult login(@RequestBody User user, HttpServletResponse response) {

        List<User> userList = userService.findUser(user);
        if (userList.size()!=0){
            String token = UUID.randomUUID().toString();
            user.setToken(token);
            userService.createOrUpdate(user);

            response.addCookie(new Cookie("token",token));

            List<User> users = userService.findUser(user);
            User dbuser=users.get(0);
            return CommonResult.success(dbuser);
        }else {
            return CommonResult.failed("用户名或密码错误！");
        }
    }

    @CrossOrigin
    @RequestMapping(value = "/api/logon", method = RequestMethod.POST,  produces = "application/json; charset=UTF-8")
    @ResponseBody
    public CommonResult logon(@RequestBody User user, HttpServletResponse response) {

        List<User> userList = userService.findUser(user);
        if (userList.size()==0){
            String token = UUID.randomUUID().toString();
            user.setToken(token);
            userService.createOrUpdate(user);

            response.addCookie(new Cookie("token",token));

            List<User> users = userService.findUser(user);
            User dbuser=users.get(0);
            return CommonResult.success(dbuser);
        }else {
            return CommonResult.failed("用户已存在！");
        }

    }

    @CrossOrigin
    @GetMapping("/api/logout")
    @ResponseBody
    public CommonResult logout(HttpServletResponse response){
        Cookie cookie = new Cookie("token",null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return CommonResult.success("注销登录成功!");
    }
}
