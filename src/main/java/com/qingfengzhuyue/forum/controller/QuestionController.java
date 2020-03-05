package com.qingfengzhuyue.forum.controller;

import com.qingfengzhuyue.forum.dto.QuestionDTO;
import com.qingfengzhuyue.forum.result.CommonResult;
import com.qingfengzhuyue.forum.result.ResultCode;
import com.qingfengzhuyue.forum.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @CrossOrigin
    @RequestMapping(value = "/api/count", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
    @ResponseBody
    public CommonResult count(){

        Long total = questionService.countQuestion();

        return CommonResult.success(total);
    }

    @CrossOrigin
    @RequestMapping(value = "/api/question", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
    @ResponseBody
    public CommonResult findQuestion(@RequestParam(name = "pageNum",defaultValue = "1") Integer pageNum,
                    @RequestParam(name = "pageSize",defaultValue = "10") Integer pageSize){

        List<QuestionDTO> questionDTOList= questionService.list(pageNum,pageSize);

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
    @CrossOrigin
    @RequestMapping(value = "/api/q/{id}/like", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
    @ResponseBody
    public CommonResult findQuestion(@PathVariable(name = "id") Long id,@RequestParam(name = "liked") Boolean liked){

        questionService.updateByLikeCount(id,liked);

        return CommonResult.success(ResultCode.SUCCESS.getMessage());
    }
}
