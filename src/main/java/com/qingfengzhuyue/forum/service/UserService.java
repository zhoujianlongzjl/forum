package com.qingfengzhuyue.forum.service;

import com.github.pagehelper.PageHelper;
import com.qingfengzhuyue.forum.mapper.CommentMapper;
import com.qingfengzhuyue.forum.mapper.QuestionMapper;
import com.qingfengzhuyue.forum.mapper.UserMapper;
import com.qingfengzhuyue.forum.model.*;
import com.qingfengzhuyue.forum.result.ResultCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private CommentMapper commentMapper;

    public UserService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public List<User> findUser(User user) {

        UserExample userExample = new UserExample();
        userExample.createCriteria()
                .andNameEqualTo(user.getName())
                .andPasswordEqualTo(user.getPassword())
                .andTypeEqualTo(user.getType());
        List<User> users = userMapper.selectByExample(userExample);

        return users;
    }

    public List<User> findUserName(User user) {

        UserExample userExample = new UserExample();
        userExample.createCriteria()
                .andNameEqualTo(user.getName())
                .andTypeEqualTo(user.getType());
        List<User> users = userMapper.selectByExample(userExample);

        return users;
    }

    public void createOrUpdate(User user) {
        UserExample userExample = new UserExample();
        userExample.createCriteria()
                .andNameEqualTo(user.getName())
                .andPasswordEqualTo(user.getPassword())
                .andTypeEqualTo(user.getType());
        List<User> users = userMapper.selectByExample(userExample);
        if (users.size() == 0) {
            //插入
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            userMapper.insert(user);
        } else {
            //更新
            User dbuser = users.get(0);
            User updateUser = new User();
            updateUser.setGmtModified(System.currentTimeMillis());
            updateUser.setAvatarUrl(user.getAvatarUrl());
            updateUser.setName(user.getName());
            updateUser.setToken(user.getToken());
            UserExample example = new UserExample();
            example.createCriteria()
                    .andIdEqualTo(dbuser.getId());
            userMapper.updateByExampleSelective(updateUser, example);

        }
    }

    public Long countQuestion(Long id,Long userId) {

        QuestionExample example = new QuestionExample();
        if (userId == id){
            example.createCriteria()
                    .andCreatorEqualTo(id);
        } else {
            example.createCriteria()
                    .andCreatorEqualTo(id)
                    .andShieldEqualTo(0)
                    .andExamineEqualTo(1);
        }
        long total = questionMapper.countByExample(example);
        return total;
    }

    public Long countComment(Long id) {

        CommentExample example = new CommentExample();
        example.createCriteria()
                .andCommentatorEqualTo(id);
        long total = commentMapper.countByExample(example);
        return total;
    }

    public List<Question> findQuestion(Integer pageNum, Integer pageSize, Long id,Long userId) {
        QuestionExample example = new QuestionExample();
        if (userId == id){
            example.createCriteria()
                    .andCreatorEqualTo(id);
            example.setOrderByClause("gmt_create desc");
            PageHelper.startPage(pageNum, pageSize);
        } else {
            example.createCriteria()
                    .andCreatorEqualTo(id)
                    .andShieldEqualTo(0)
                    .andExamineEqualTo(1);
            example.setOrderByClause("gmt_create desc");
            PageHelper.startPage(pageNum, pageSize);
        }
        List<Question> questionList = questionMapper.selectByExample(example);

        return questionList;
    }

    public List<Question> findComment(Integer pageNum, Integer pageSize, Long id) {
        CommentExample example = new CommentExample();
        example.createCriteria()
                .andCommentatorEqualTo(id);
        example.setOrderByClause("gmt_create desc");
        PageHelper.startPage(pageNum, pageSize);
        List<Comment> commentList = commentMapper.selectByExample(example);
        List<Question> questionList = new ArrayList<>();
        boolean repeat = false; //判断问题是否重复，false不重复，true重复
        for (Comment comment : commentList) {
            if (comment.getType() == ResultCode.REPLY_QUESTION.getCode()) {
                Question question = questionMapper.selectByPrimaryKey(comment.getParentId());
                for (Question question1 : questionList) {
                    if (question1.getId() == question.getId()) {
                        repeat = true;
                        break;
                    }
                }
                // 是否屏蔽，是否审核通过
                if (question.getShield() != 0 || question.getExamine() != 1){
                    repeat = true;
                }
                if (repeat == false) {
                    questionList.add(question);
                }
                repeat=false;
            } else {
                Comment commentParent = commentMapper.selectByPrimaryKey(comment.getParentId());
                Question question = questionMapper.selectByPrimaryKey(commentParent.getParentId());
                for (Question question1 : questionList) {
                    if (question1.getId() == question.getId()) {
                        repeat = true;
                        break;
                    }
                }
                // 是否屏蔽，是否审核通过
                if (question.getShield() != 0 || question.getExamine() != 1){
                    repeat = true;
                }
                if (repeat == false) {
                    questionList.add(question);
                }
                repeat=false;
            }
        }
        return questionList;
    }

    public User findByUserId(Long id) {
        User user = userMapper.selectByPrimaryKey(id);
        return user;
    }

    public User insertBio(User frontUser) {
        User user = userMapper.selectByPrimaryKey(frontUser.getId());
        user.setBio(frontUser.getBio());
        UserExample example = new UserExample();
        example.createCriteria()
                .andIdEqualTo(user.getId());
        userMapper.updateByExampleSelective(user, example);
        User dbUser = userMapper.selectByPrimaryKey(frontUser.getId());
        return dbUser;
    }

    public void updateUserAvatar(String imageName,Long userId) {

        User user = userMapper.selectByPrimaryKey(userId);
        user.setAvatarUrl(imageName);

        UserExample example = new UserExample();
        example.createCriteria()
                .andIdEqualTo(userId);
        userMapper.updateByExampleSelective(user, example);
    }

    public List<User> findNotId(Long id) {
        UserExample example = new UserExample();
        example.createCriteria()
                .andIdGreaterThanOrEqualTo(0l)
                .andIdNotEqualTo(id);
        List<User> users = userMapper.selectByExample(example);
        return users;
    }

    public void delete(Long id) {
        userMapper.deleteByPrimaryKey(id);
    }

    public void passwordUpdate(User dbUser) {
        UserExample example = new UserExample();
        example.createCriteria()
                .andIdEqualTo(dbUser.getId());
        userMapper.updateByExampleSelective(dbUser, example);
    }
}
