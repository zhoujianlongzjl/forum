package com.qingfengzhuyue.forum.controller;

import com.qingfengzhuyue.forum.cache.TagCache;
import com.qingfengzhuyue.forum.model.Question;
import com.qingfengzhuyue.forum.model.Tag;
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
public class PublishController {

    @Autowired
    private QuestionService questionService;

    /**
     * 获取缓存标签
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/api/publish", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
    @ResponseBody
    public CommonResult getTagDTO() {
        return CommonResult.success(TagCache.get());
    }

    /**
     * 获取数据库里所有标签
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/api/publish/getTags", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
    @ResponseBody
    public CommonResult getTags() {
        List<Tag> tags = questionService.getTags();
        return CommonResult.success(tags);
    }

    /**
     * 发布问题
     * @param question
     * @param request
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/api/publish", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
    @ResponseBody
    public CommonResult publish(@RequestBody Question question, HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return CommonResult.unauthorized("");
        }
        question.setCreator(user.getId());
        questionService.createOrUpdate(question);
        return CommonResult.success(ResultCode.SUCCESS.getMessage());
    }

    /**
     * 编辑问题
     * @param id
     * @param request
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/api/publish/{id}", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
    @ResponseBody
    public CommonResult edit(@PathVariable(name = "id") Long id, HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return CommonResult.unauthorized("");
        }
        Question question = questionService.edit(id);
        if (question == null){
           return CommonResult.failed(ResultCode.QUESTION_NOT_FOUND);
        }
        if (user.getId() != question.getCreator()){
            return CommonResult.failed(ResultCode.NOT_YOUR_QUESTION);
        }
        return CommonResult.success(question);
    }
}
