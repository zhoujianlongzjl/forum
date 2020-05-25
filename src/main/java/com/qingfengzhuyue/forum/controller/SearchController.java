package com.qingfengzhuyue.forum.controller;

import com.qingfengzhuyue.forum.model.Question;
import com.qingfengzhuyue.forum.model.User;
import com.qingfengzhuyue.forum.result.CommonResult;
import com.qingfengzhuyue.forum.result.ResultCode;
import com.qingfengzhuyue.forum.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class SearchController {
    @Autowired
    private SearchService searchService;

    /**
     * 查询搜索问题数
     * @param search
     * @param request
     * @param searchType
     * @return
     */
    @RequestMapping(value = "/api/searchCount", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
    @ResponseBody
    public CommonResult searchCount(@RequestParam(name = "search",required = false,defaultValue = ".*") String search,
                                    HttpServletRequest request,
                                    @RequestParam(name = "searchType",defaultValue = "0") Integer searchType){

        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return CommonResult.unauthorized("");
        }
        Long total = searchService.countBySearch(search,user.getType(),searchType);
        if (total == 0) {
            return CommonResult.failed(ResultCode.QUESTION_NOT_FOUND);
        }
        return CommonResult.success(total);
    }

    /**
     * 搜索问题
     * @param pageNum
     * @param pageSize
     * @param search
     * @param request
     * @param searchType
     * @return
     */
    @RequestMapping(value = "/api/search", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
    @ResponseBody
    public CommonResult search( @RequestParam(name = "pageNum",defaultValue = "1") Integer pageNum,
                                @RequestParam(name = "pageSize",defaultValue = "10") Integer pageSize,
                                @RequestParam(name = "search",required = false,defaultValue = ".*") String search,
                                HttpServletRequest request,
                                @RequestParam(name = "searchType",defaultValue = "0") Integer searchType){
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return CommonResult.unauthorized("");
        }
        List<Question> questionList = searchService.selectBySearch(search,pageNum,pageSize,user.getType(),searchType);
        return CommonResult.success(questionList);
    }
}
