package com.qingfengzhuyue.forum.controller;

import com.qingfengzhuyue.forum.cache.PopularQuestionCache;
import com.qingfengzhuyue.forum.dto.PopularQuestionDTO;
import com.qingfengzhuyue.forum.dto.QuestionDTO;
import com.qingfengzhuyue.forum.model.User;
import com.qingfengzhuyue.forum.result.CommonResult;
import com.qingfengzhuyue.forum.result.ResultCode;
import com.qingfengzhuyue.forum.scheduled.ScheduledService;
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

    @Autowired
    private ScheduledService scheduledService;

    @CrossOrigin
    @RequestMapping(value = "/api/count", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
    @ResponseBody
    public CommonResult count(HttpServletRequest request) {

        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return CommonResult.unauthorized("");
        }
        Long total = questionService.countQuestion(user.getType());

        return CommonResult.success(total);
    }

    /**
     * 最新问答
     * @param pageNum
     * @param pageSize
     * @param request
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/api/question", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
    @ResponseBody
    public CommonResult findQuestion(@RequestParam(name = "pageNum", defaultValue = "1") Integer pageNum,
                                     @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                     HttpServletRequest request) {

        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return CommonResult.unauthorized("");
        }
        List<QuestionDTO> questionDTOList = questionService.list(pageNum, pageSize, user.getType());

        return CommonResult.success(questionDTOList);
    }

    @CrossOrigin
    @RequestMapping(value = "/api/waitCount", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
    @ResponseBody
    public CommonResult waitCount(HttpServletRequest request) {

        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return CommonResult.unauthorized("");
        }
        Long total = questionService.waitCount();

        return CommonResult.success(total);
    }

    @CrossOrigin
    @RequestMapping(value = "/api/wait", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
    @ResponseBody
    public CommonResult wait(@RequestParam(name = "pageNum", defaultValue = "1") Integer pageNum,
                                     @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                     HttpServletRequest request) {

        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return CommonResult.unauthorized("");
        }
        List<QuestionDTO> questionDTOList = questionService.findWait(pageNum,pageSize);
        return CommonResult.success(questionDTOList);
    }

    /**
     *  热门问题
     * @param pageNum
     * @param pageSize
     * @param request
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/api/popularQuestion", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
    @ResponseBody
    public CommonResult popularQuestion(@RequestParam(name = "pageNum", defaultValue = "1") Integer pageNum,
                                        @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                        HttpServletRequest request) {

        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return CommonResult.unauthorized("");
        }
        if (PopularQuestionCache.isEmpty()) {
            scheduledService.scheduled();
        }
        List<PopularQuestionDTO> popularQuestionDTOS = PopularQuestionCache.getPopularQuestionDTOS();

        Integer fromIndex = (pageNum - 1) * pageSize;
        Integer toIndex = pageNum * pageSize;
        Long total = questionService.countQuestion(user.getType());
        if (toIndex > total) {
            toIndex = Math.toIntExact(total);
        }
        List<PopularQuestionDTO> popularQuestionDTOList = popularQuestionDTOS.subList(fromIndex, toIndex);
        return CommonResult.success(popularQuestionDTOList);
    }


    @CrossOrigin
    @RequestMapping(value = "/api/q/{id}", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
    @ResponseBody
    public CommonResult question(@PathVariable(name = "id") Long id, HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return CommonResult.unauthorized("");
        }
        QuestionDTO questionDTO = questionService.findById(id);
        //累加阅读数
        questionService.incView(id);
        return CommonResult.success(questionDTO);
    }

    //点赞
    @CrossOrigin
    @RequestMapping(value = "/api/q/{id}/like", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
    @ResponseBody
    public CommonResult findQuestion(@PathVariable(name = "id") Long id, @RequestParam(name = "liked") Boolean liked,
                                     HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return CommonResult.unauthorized("");
        }
        questionService.updateByLikeCount(id, liked);

        return CommonResult.success(ResultCode.SUCCESS.getMessage());
    }

    /**
     * 管理员屏蔽问题
     *
     * @param id      问题Id
     * @param request
     * @return
     */
    @RequestMapping(value = "/api/admin/shield", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
    @ResponseBody
    public CommonResult shield(@RequestParam("id") Long id, @RequestParam("shield") Integer shield, HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return CommonResult.unauthorized("");
        }
        if (user.getType() != 0) {
            questionService.shield(id, shield);
            return CommonResult.success("");
        }
        return CommonResult.failed("");
    }

    /**
     * 管理员审核问题
     *
     * @param id
     * @param examine
     * @param request
     * @return
     */
    @RequestMapping(value = "/api/admin/examine", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
    @ResponseBody
    public CommonResult examine(@RequestParam("id") Long id, @RequestParam("examine") Integer examine, HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return CommonResult.unauthorized("");
        }
        if (user.getType() != 0) {
            questionService.examine(id, examine);
            return CommonResult.success("");
        }
        return CommonResult.failed("");
    }

    @RequestMapping(value = "/api/admin/findByExamine", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
    @ResponseBody
    public CommonResult findByExamine(@RequestParam(name = "pageNum", defaultValue = "1") Integer pageNum,
                                      @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                      HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return CommonResult.unauthorized("");
        }
        if (user.getType() != 0) {
            List<QuestionDTO> questionDTOList = questionService.findByExamine(pageNum, pageSize);
            return CommonResult.success(questionDTOList);
        }
        return CommonResult.failed("");
    }

    @RequestMapping(value = "/api/admin/q/delete", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
    @ResponseBody
    public CommonResult delete(@RequestParam("id") Long id, HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return CommonResult.unauthorized("");
        }
        if (user.getType() != 0) {
            questionService.delete(id);
            return CommonResult.success("");
        }
        QuestionDTO questionDTO = questionService.findById(id);
        if (questionDTO.getCreator() == user.getId()) {
            questionService.delete(id);
            return CommonResult.success("");
        }
        return CommonResult.failed("");
    }
}
