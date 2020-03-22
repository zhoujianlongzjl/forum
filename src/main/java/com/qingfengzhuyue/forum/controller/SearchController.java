package com.qingfengzhuyue.forum.controller;

import com.qingfengzhuyue.forum.model.Question;
import com.qingfengzhuyue.forum.result.CommonResult;
import com.qingfengzhuyue.forum.result.ResultCode;
import com.qingfengzhuyue.forum.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class SearchController {
    @Autowired
    private SearchService searchService;

    @RequestMapping(value = "/api/searchCount", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
    @ResponseBody
    public CommonResult searchCount(@RequestParam(name = "search",required = false,defaultValue = ".*") String search){

        Integer total = searchService.countBySearch(search);
        if (total == 0) {
            return CommonResult.failed(ResultCode.QUESTION_NOT_FOUND);
        }
        return CommonResult.success(total);
    }

    @RequestMapping(value = "/api/search", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
    @ResponseBody
    public CommonResult search( @RequestParam(name = "pageNum",defaultValue = "1") Integer pageNum,
                                @RequestParam(name = "pageSize",defaultValue = "10") Integer pageSize,
                                @RequestParam(name = "search",required = false,defaultValue = ".*") String search){

        List<Question> questionList = searchService.selectBySearch(search,pageNum,pageSize);
        return CommonResult.success(questionList);
    }
}
