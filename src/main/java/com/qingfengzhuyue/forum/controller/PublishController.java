package com.qingfengzhuyue.forum.controller;

import com.qingfengzhuyue.forum.cache.TagCache;
import com.qingfengzhuyue.forum.dto.QuestionDTO;
import com.qingfengzhuyue.forum.model.Question;
import com.qingfengzhuyue.forum.result.CommonResult;
import com.qingfengzhuyue.forum.result.ResultCode;
import com.qingfengzhuyue.forum.service.QuestionService;
import com.qingfengzhuyue.forum.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
public class PublishController {

    @Autowired
    private QuestionService questionService;

    @CrossOrigin
    @RequestMapping(value = "/api/publish", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
    @ResponseBody
    public CommonResult getTagDTO() {
        return CommonResult.success(TagCache.get());
    }

    @CrossOrigin
    @RequestMapping(value = "/api/publish", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
    @ResponseBody
    public CommonResult publish(@RequestBody Question question) {
        question.setCreator(question.getCreator());
        questionService.createOrUpdate(question);
        return CommonResult.success(ResultCode.SUCCESS.getMessage());
    }
    @CrossOrigin
    @RequestMapping(value = "/api/publish/{id}", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
    @ResponseBody
    public CommonResult edit(@PathVariable(name = "id") Long id){
        Question question = questionService.edit(id);
        return CommonResult.success(question);
    }
}
