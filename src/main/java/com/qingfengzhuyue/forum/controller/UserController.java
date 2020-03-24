package com.qingfengzhuyue.forum.controller;

import com.qingfengzhuyue.forum.model.Question;
import com.qingfengzhuyue.forum.model.User;
import com.qingfengzhuyue.forum.result.CommonResult;
import com.qingfengzhuyue.forum.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/api/user/{id}", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
    @ResponseBody
    public CommonResult userId(@PathVariable(name = "id") Long id,HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return CommonResult.unauthorized("");
        }
        User dbUser = userService.findByUserId(id);
        return CommonResult.success(dbUser);
    }

    @RequestMapping(value = "/api/user/countQ", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
    @ResponseBody
    public CommonResult countQ(@RequestParam(name = "id") Long id, HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return CommonResult.unauthorized("");
        }
        Long total = userService.countQuestion(id);

        return CommonResult.success(total);
    }
    @RequestMapping(value = "/api/user/countC", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
    @ResponseBody
    public CommonResult countC(@RequestParam(name = "id") Long id,HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return CommonResult.unauthorized("");
        }
        Long total = userService.countComment(id);

        return CommonResult.success(total);
    }

    @RequestMapping(value = "/api/user/question", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
    @ResponseBody
    public CommonResult findQuestion(@RequestParam(name = "pageNum",defaultValue = "1") Integer pageNum,
                                     @RequestParam(name = "pageSize",defaultValue = "10") Integer pageSize,
                                     @RequestParam(name = "id") Long id,
                                     HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return CommonResult.unauthorized("");
        }
        List<Question> questionList = userService.findQuestion(pageNum,pageSize,id);

        return CommonResult.success(questionList);
    }
    @RequestMapping(value = "/api/user/comment", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
    @ResponseBody
    public CommonResult findComment(@RequestParam(name = "pageNum",defaultValue = "1") Integer pageNum,
                                     @RequestParam(name = "pageSize",defaultValue = "10") Integer pageSize,
                                    @RequestParam(name = "id") Long id,
                                    HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return CommonResult.unauthorized("");
        }
        List<Question> questionList = userService.findComment(pageNum,pageSize,id);

        return CommonResult.success(questionList);
    }
    @RequestMapping(value = "/api/user/bio", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
    @ResponseBody
    public CommonResult bio(@RequestBody User frontUser, HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return CommonResult.unauthorized("");
        }
        User dbUser = userService.insertBio(frontUser);

        return CommonResult.success(dbUser);
    }

    @RequestMapping(value = "/api/admin/user/find", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
    @ResponseBody
    public CommonResult findNotId(HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return CommonResult.unauthorized("");
        }
        List<User> dbUser = userService.findNotId(user.getId());

        return CommonResult.success(dbUser);
    }

    @RequestMapping(value = "/api/admin/user/delete", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
    @ResponseBody
    public CommonResult delete(@RequestParam("id") Long id, HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return CommonResult.unauthorized("");
        }
        userService.delete(id);

        return CommonResult.success("");
    }

}
