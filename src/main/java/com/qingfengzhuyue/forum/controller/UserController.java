package com.qingfengzhuyue.forum.controller;

import com.qingfengzhuyue.forum.dto.ChangePasswordDTO;
import com.qingfengzhuyue.forum.model.Question;
import com.qingfengzhuyue.forum.model.User;
import com.qingfengzhuyue.forum.result.CommonResult;
import com.qingfengzhuyue.forum.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.DigestUtils;
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
        Long total = userService.countQuestion(id,user.getId());

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

    /**
     * 查询我的问题
     * @param pageNum
     * @param pageSize
     * @param id
     * @param request
     * @return
     */
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
        List<Question> questionList = userService.findQuestion(pageNum,pageSize,id,user.getId());

        return CommonResult.success(questionList);
    }

    /**
     * 查询我的评论
     * @param pageNum
     * @param pageSize
     * @param id
     * @param request
     * @return
     */
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

    /**
     * 添加简介
     * @param frontUser
     * @param request
     * @return
     */
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

    /**
     * 修改密码
     * @param changePasswordDTO
     * @param request
     * @return
     */
    @RequestMapping(value = "/api/user/changePassword", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
    @ResponseBody
    public CommonResult changePassword(@RequestBody ChangePasswordDTO changePasswordDTO, HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return CommonResult.unauthorized("");
        }
        String md5OldPass = DigestUtils.md5DigestAsHex(changePasswordDTO.getOldPassword().getBytes());
        changePasswordDTO.setOldPassword(md5OldPass);
        User dbUser = userService.findByUserId(user.getId());
        if (dbUser.getPassword().equals(changePasswordDTO.getOldPassword())){
            String md5Pass = DigestUtils.md5DigestAsHex(changePasswordDTO.getNewPassword().getBytes());
            changePasswordDTO.setNewPassword(md5Pass);
            dbUser.setPassword(changePasswordDTO.getNewPassword());
            userService.passwordUpdate(dbUser);
            return CommonResult.success("修好密码成功！");
        }
        return CommonResult.failed("原密码错误");
    }

    /**
     * 管理员查询用户
     * @param request
     * @return
     */
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

    /**
     * 管理员删除用户
     * @param id
     * @param request
     * @return
     */
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
