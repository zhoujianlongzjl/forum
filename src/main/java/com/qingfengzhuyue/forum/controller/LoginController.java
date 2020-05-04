package com.qingfengzhuyue.forum.controller;

import com.qingfengzhuyue.forum.model.User;
import com.qingfengzhuyue.forum.result.CommonResult;
import com.qingfengzhuyue.forum.result.ResultCode;
import com.qingfengzhuyue.forum.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.UUID;

@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    @CrossOrigin
    @RequestMapping(value = "/api/login", method = RequestMethod.POST,  produces = "application/json; charset=UTF-8")
    @ResponseBody
    public CommonResult login(@RequestBody User user,HttpServletRequest request, HttpServletResponse response) {
        String md5Pass = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
        user.setPassword(md5Pass);
        List<User> userList = userService.findUser(user);
        if (userList.size()!=0){
            String token = UUID.randomUUID().toString();
            user.setToken(token);
            userService.createOrUpdate(user);

            Cookie cookie = new Cookie("token", token);
            response.addCookie(cookie);

            List<User> users = userService.findUser(user);
            User dbuser=users.get(0);
            request.getSession().setAttribute("user",users.get(0));
            return CommonResult.success(dbuser,ResultCode.LOGIN_SUCCESS.getMessage());
        }else {
            return CommonResult.failed(ResultCode.WRONG_USER_NAME_OR_PASSWORD);
        }
    }

    @CrossOrigin
    @RequestMapping(value = "/api/logon", method = RequestMethod.POST,  produces = "application/json; charset=UTF-8")
    @ResponseBody
    public CommonResult logon(@RequestBody User user,HttpServletRequest request, HttpServletResponse response) {

        List<User> userList = userService.findUserName(user);
        String md5Pass = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
        user.setPassword(md5Pass);
        if (userList.size()==0){
            String token = UUID.randomUUID().toString();
            user.setToken(token);
            userService.createOrUpdate(user);

            Cookie cookie = new Cookie("token", token);
            response.addCookie(cookie);

            List<User> users = userService.findUser(user);
            User dbuser=users.get(0);
            request.getSession().setAttribute("user",users.get(0));
            return CommonResult.success(dbuser,ResultCode.REGISTER_SUCCESS.getMessage());
        }else {
            return CommonResult.failed(ResultCode.USER_ALREADY_EXISTS);
        }
    }

    @CrossOrigin
    @RequestMapping(value = "/api/admin/logon", method = RequestMethod.POST,  produces = "application/json; charset=UTF-8")
    @ResponseBody
    public CommonResult adminLogon(@RequestBody User user) {

        List<User> userList = userService.findUserName(user);
        String md5Pass = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
        user.setPassword(md5Pass);
        if (userList.size()==0){
            userService.createOrUpdate(user);
            return CommonResult.success("");
        }else {
            return CommonResult.failed(ResultCode.USER_ALREADY_EXISTS);
        }

    }
    @CrossOrigin
    @GetMapping("/api/logout")
    @ResponseBody
    public CommonResult logout(HttpServletRequest request, HttpServletResponse response){
        request.getSession().removeAttribute("user");
        Cookie cookie = new Cookie("token",null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return CommonResult.success("注销登录成功!");
    }
}
