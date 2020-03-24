package com.qingfengzhuyue.forum.controller;

import com.qingfengzhuyue.forum.dto.QuestionDTO;
import com.qingfengzhuyue.forum.model.User;
import com.qingfengzhuyue.forum.result.CommonResult;
import com.qingfengzhuyue.forum.result.ResultCode;
import com.qingfengzhuyue.forum.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @CrossOrigin
    @RequestMapping(value = "/api/count", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
    @ResponseBody
    public CommonResult count(@RequestParam(name = "userType",defaultValue = "0") Long userType){

        Long total = questionService.countQuestion(userType);

        return CommonResult.success(total);
    }

    @CrossOrigin
    @RequestMapping(value = "/api/question", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
    @ResponseBody
    public CommonResult findQuestion(@RequestParam(name = "pageNum",defaultValue = "1") Integer pageNum,
                                     @RequestParam(name = "pageSize",defaultValue = "10") Integer pageSize,
                                     @RequestParam(name = "userType",defaultValue = "0") Long userType){

        List<QuestionDTO> questionDTOList= questionService.list(pageNum,pageSize,userType);

        return CommonResult.success(questionDTOList);
    }


    @CrossOrigin
    @RequestMapping(value = "/api/q/{id}", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
    @ResponseBody
    public CommonResult question(@PathVariable(name = "id") Long id){
        QuestionDTO questionDTO = questionService.findById(id);
        //累加阅读数
        questionService.incView(id);
        return CommonResult.success(questionDTO);
    }

    //点赞
    @CrossOrigin
    @RequestMapping(value = "/api/q/{id}/like", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
    @ResponseBody
    public CommonResult findQuestion(@PathVariable(name = "id") Long id,@RequestParam(name = "liked") Boolean liked){

        questionService.updateByLikeCount(id,liked);

        return CommonResult.success(ResultCode.SUCCESS.getMessage());
    }

    /**
     * 管理员屏蔽问题
     * @param id   问题Id
     * @param request
     * @return
     */
    @RequestMapping(value = "/api/admin/shield", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
    @ResponseBody
    public CommonResult shield(@RequestParam("id") Long id,@RequestParam("shield") Integer shield, HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return CommonResult.unauthorized("");
        }
        if(user.getType() != 0){
            questionService.shield(id,shield);
            return CommonResult.success("");
        }
        return CommonResult.failed("");
    }

    @RequestMapping(value = "/api/admin/q/delete", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
    @ResponseBody
    public CommonResult delete(@RequestParam("id") Long id,HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return CommonResult.unauthorized("");
        }
        if(user.getType() != 0){
            questionService.delete(id);
            return CommonResult.success("");
        }
        return CommonResult.failed("");
    }
}
