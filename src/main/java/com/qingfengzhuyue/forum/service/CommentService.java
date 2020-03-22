package com.qingfengzhuyue.forum.service;

import com.qingfengzhuyue.forum.dto.CommentDTO;
import com.qingfengzhuyue.forum.mapper.*;
import com.qingfengzhuyue.forum.model.Comment;
import com.qingfengzhuyue.forum.model.CommentExample;
import com.qingfengzhuyue.forum.model.Question;
import com.qingfengzhuyue.forum.model.User;
import com.qingfengzhuyue.forum.result.CommonResult;
import com.qingfengzhuyue.forum.result.ResultCode;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentService {

    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private QuestionExtMapper questionExtMapper;
    @Autowired
    private CommentExtMapper commentExtMapper;

    public CommonResult insert(Comment comment) {
        if (comment.getParentId() == null || comment.getParentId() == 0) {
            return CommonResult.failed(ResultCode.TARGET_PARAM_NOT_FOUND);
        }
        if (comment.getType() == null) {
            return CommonResult.failed(ResultCode.TYPE_PARAM_WRONG);
        }

        if (comment.getType() == ResultCode.REPLY_QUESTION.getCode()) {
            //回复问题
            Question question = questionMapper.selectByPrimaryKey(comment.getParentId());
            if (question == null) {
                return CommonResult.failed(ResultCode.QUESTION_NOT_FOUND);
            }
            //创建评论
            commentMapper.insert(comment);
            //增加评论数
            question.setCommentCount(1);
            questionExtMapper.incCommentCount(question);

            CommentExample commentExample = new CommentExample();
            commentExample.createCriteria()
                    .andParentIdEqualTo(comment.getParentId())
                    .andTypeEqualTo(comment.getType());
            List<Comment> comments = commentMapper.selectByExample(commentExample);
            List<CommentDTO> commentDTOList = new ArrayList<>();
            for (Comment dbComment : comments) {
                User user = userMapper.selectByPrimaryKey(dbComment.getCommentator());
                CommentDTO commentDTO = new CommentDTO();
                BeanUtils.copyProperties(dbComment, commentDTO);
                commentDTO.setUser(user);
                commentDTOList.add(commentDTO);
            }
            return CommonResult.success(commentDTOList);
        } else if (comment.getType() == ResultCode.REPLY_COMMENT.getCode()) {
            //回复评论
            Comment dbcomment = commentMapper.selectByPrimaryKey(comment.getParentId());
            if (dbcomment == null) {
                return CommonResult.failed(ResultCode.COMMENT_NOT_FOUND);
            }
            //创建评论
            commentMapper.insert(comment);
            //增加评论数
            dbcomment.setCommentCount(1L);
            commentExtMapper.incCommentCount(dbcomment);

            CommentExample commentExample = new CommentExample();
            commentExample.createCriteria()
                    .andParentIdEqualTo(comment.getParentId())
                    .andTypeEqualTo(comment.getType());
            List<Comment> comments = commentMapper.selectByExample(commentExample);
            List<CommentDTO> commentDTOList = new ArrayList<>();
            for (Comment dbComment : comments) {
                User user = userMapper.selectByPrimaryKey(dbComment.getCommentator());
                CommentDTO commentDTO = new CommentDTO();
                BeanUtils.copyProperties(dbComment, commentDTO);
                commentDTO.setUser(user);
                commentDTOList.add(commentDTO);
            }
            return CommonResult.success(commentDTOList);
        }
        return null;
    }

    public CommonResult initComment(Long id,Integer type) {
        CommentExample example = new CommentExample();
        example.createCriteria()
                .andParentIdEqualTo(id)
                .andTypeEqualTo(type);
        List<Comment> comments = commentMapper.selectByExample(example);
        List<CommentDTO> commentDTOList = new ArrayList<>();
        for (Comment dbComment : comments) {
            User user = userMapper.selectByPrimaryKey(dbComment.getCommentator());
            CommentDTO commentDTO = new CommentDTO();
            BeanUtils.copyProperties(dbComment, commentDTO);
            commentDTO.setUser(user);
            commentDTOList.add(commentDTO);
        }
        return CommonResult.success(commentDTOList);
    }
}
