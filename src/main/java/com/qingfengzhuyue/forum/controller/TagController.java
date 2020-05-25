package com.qingfengzhuyue.forum.controller;

import com.qingfengzhuyue.forum.model.Tag;
import com.qingfengzhuyue.forum.model.User;
import com.qingfengzhuyue.forum.result.CommonResult;
import com.qingfengzhuyue.forum.result.ResultCode;
import com.qingfengzhuyue.forum.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class TagController {

    @Autowired
    private TagService tagService;

    @CrossOrigin
    @RequestMapping(value = "/api/tag/{id}", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
    @ResponseBody
    public CommonResult tagId(@PathVariable(name = "id") Long id,HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return CommonResult.unauthorized("");
        }
        Tag tag = tagService.findById(id);
        return CommonResult.success(tag);
    }

    @RequestMapping(value = "/api/countTag", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
    @ResponseBody
    public CommonResult countTag(HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return CommonResult.unauthorized("");
        }
        Long total = tagService.countTag();
        return CommonResult.success(total);
    }
    @RequestMapping(value = "/api/findTag", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
    @ResponseBody
    public CommonResult findTag(HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return CommonResult.unauthorized("");
        }
        List<Tag> tags = tagService.findAll();

        return CommonResult.success(tags);
    }

    @CrossOrigin
    @RequestMapping(value = "/api/findTagPage", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
    @ResponseBody
    public CommonResult findTagPage(@RequestParam(name = "pageNum",defaultValue = "1") Integer pageNum,
                                     @RequestParam(name = "pageSize",defaultValue = "14") Integer pageSize){

        List<Tag> tags = tagService.findTagPage(pageNum,pageSize);
        return CommonResult.success(tags);
    }

    /**
     * 添加标签
     * @param tag
     * @param request
     * @return
     */
    @RequestMapping(value = "/api/admin/addTag", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
    @ResponseBody
    public CommonResult addTag(@RequestBody Tag tag,HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return CommonResult.unauthorized("");
        }
        List<Tag> tagList = tagService.findName(tag.getName());
        if (tagList.size()!=0){
            return CommonResult.failed(ResultCode.TAG_EXISTS);
        }
        tagService.addTag(tag);
        return CommonResult.success("");
    }

    /**
     * 删除标签
     * @param tagId
     * @param request
     * @return
     */
    @RequestMapping(value = "/api/admin/deleteTag", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
    @ResponseBody
    public CommonResult deleteTag(@RequestParam("tagId") Long tagId, HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return CommonResult.unauthorized("");
        }
        if(user.getType() != 0){
            tagService.deleteTag(tagId);
            return CommonResult.success("");
        }
        return CommonResult.failed(ResultCode.NOT_YOUR_ADMIN);
    }
}
