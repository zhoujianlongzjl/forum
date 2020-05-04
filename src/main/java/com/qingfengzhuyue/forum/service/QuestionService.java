package com.qingfengzhuyue.forum.service;

import com.github.pagehelper.PageHelper;
import com.qingfengzhuyue.forum.dto.PopularQuestionDTO;
import com.qingfengzhuyue.forum.dto.QuestionDTO;
import com.qingfengzhuyue.forum.mapper.QuestionExtMapper;
import com.qingfengzhuyue.forum.mapper.QuestionMapper;
import com.qingfengzhuyue.forum.mapper.TagMapper;
import com.qingfengzhuyue.forum.mapper.UserMapper;
import com.qingfengzhuyue.forum.model.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {

    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private QuestionExtMapper questionExtMapper;
    @Autowired
    private TagMapper tagMapper;
    @Autowired
    private UserMapper userMapper;

    public void createOrUpdate(Question question) {
        if (question.getId() == null) {
            //创建
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtCreate());
            question.setCommentCount(0);
            question.setViewCount(0);
            question.setLikeCount(0);
            question.setShield(0);
            question.setExamine(0);
            questionMapper.insert(question);
        } else {
            //更新

            Question updateQuestion = new Question();
            updateQuestion.setGmtModified(System.currentTimeMillis());
            updateQuestion.setTitle(question.getTitle());
            updateQuestion.setDescription(question.getDescription());
            updateQuestion.setTag(question.getTag());
            updateQuestion.setExamine(0);

            QuestionExample example = new QuestionExample();
            example.createCriteria()
                    .andIdEqualTo(question.getId());
            questionMapper.updateByExampleSelective(updateQuestion, example);
        }
    }

    public List<QuestionDTO> list(Integer pageNum, Integer pageSize,Integer userType) {
        if (userType != 0){
            // 管理员查询
            QuestionExample example = new QuestionExample();
            example.createCriteria()
                    .andIdGreaterThanOrEqualTo(0L);
            example.setOrderByClause("gmt_modified desc");
            PageHelper.startPage(pageNum, pageSize);
            List<Question> questionList = questionMapper.selectByExample(example);

            List<QuestionDTO> questionDTOList = new ArrayList<>();
            for (Question question : questionList) {
                User user = userMapper.selectByPrimaryKey(question.getCreator());
                QuestionDTO questionDTO = new QuestionDTO();
                BeanUtils.copyProperties(question, questionDTO);
                questionDTO.setUser(user);
                questionDTOList.add(questionDTO);
            }
            return questionDTOList;
        } else {
            // 普通用户查询
            QuestionExample example = new QuestionExample();
            example.createCriteria()
                    .andIdGreaterThanOrEqualTo(0L)
                    .andShieldEqualTo(0)
                    .andExamineEqualTo(1);
            example.setOrderByClause("gmt_modified desc");
            PageHelper.startPage(pageNum, pageSize);
            List<Question> questionList = questionMapper.selectByExample(example);

            List<QuestionDTO> questionDTOList = new ArrayList<>();
            for (Question question : questionList) {
                User user = userMapper.selectByPrimaryKey(question.getCreator());
                QuestionDTO questionDTO = new QuestionDTO();
                BeanUtils.copyProperties(question, questionDTO);
                questionDTO.setUser(user);
                questionDTOList.add(questionDTO);
            }
            return questionDTOList;
        }

    }

    public Long countQuestion(Integer userType) {
        if (userType != 0){
            // 管理员
            QuestionExample example = new QuestionExample();
            example.createCriteria()
                    .andIdGreaterThanOrEqualTo(0L);
            long total = questionMapper.countByExample(example);
            return total;
        } else {
            // 普通用户
            QuestionExample example = new QuestionExample();
            example.createCriteria()
                    .andIdGreaterThanOrEqualTo(0L)
                    .andShieldEqualTo(0)
                    .andExamineEqualTo(1);
            long total = questionMapper.countByExample(example);
            return total;
        }

    }

    public QuestionDTO findById(Long id) {
        Question question = questionMapper.selectByPrimaryKey(id);
        if (question != null){
            User user = userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(user);
            return questionDTO;
        }
        return null;
    }
    public Question edit(Long id) {
        Question question = questionMapper.selectByPrimaryKey(id);
        return question;
    }

    public void incView(Long id) {
        Question question = new Question();
        question.setId(id);
        question.setViewCount(1);
        questionExtMapper.incView(question);
    }

    public void updateByLikeCount(Long id, Boolean liked) {
        Question question = new Question();
        question.setId(id);
        question.setLikeCount(1);
        if (!liked){
            questionExtMapper.incLike(question);
        }else {
            questionExtMapper.redLike(question);
        }
    }


    public void shield(Long id, Integer shield) {
        Question question = questionMapper.selectByPrimaryKey(id);
        question.setShield(shield);
        QuestionExample example = new QuestionExample();
        example.createCriteria()
                .andIdEqualTo(question.getId());
        questionMapper.updateByExampleSelective(question, example);

    }

    public void delete(Long id) {
        questionMapper.deleteByPrimaryKey(id);
    }

    public List<Tag> getTags() {
        TagExample example = new TagExample();
        example.createCriteria()
                .andIdGreaterThanOrEqualTo(0L);
        List<Tag> tags = tagMapper.selectByExample(example);
        return tags;
    }

    public void examine(Long id, Integer examine) {
        Question question = questionMapper.selectByPrimaryKey(id);
        question.setExamine(examine);
        QuestionExample example = new QuestionExample();
        example.createCriteria()
                .andIdEqualTo(question.getId());
        questionMapper.updateByExampleSelective(question, example);
    }

    public List<QuestionDTO> findByExamine(Integer pageNum, Integer pageSize) {
        QuestionExample example = new QuestionExample();
        example.createCriteria()
                .andIdGreaterThanOrEqualTo(0L);
        example.setOrderByClause("examine asc");
        PageHelper.startPage(pageNum,pageSize);
        List<Question> questionList = questionMapper.selectByExample(example);

        List<QuestionDTO> questionDTOList = new ArrayList<>();
        for (Question question : questionList) {
            User user = userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        return questionDTOList;
    }

    public List<QuestionDTO> findWait(Integer pageNum, Integer pageSize) {
        QuestionExample example = new QuestionExample();
        example.createCriteria()
                .andCommentCountEqualTo(0)
                .andShieldEqualTo(0)
                .andExamineEqualTo(1);
        example.setOrderByClause("gmt_modified desc");
        PageHelper.startPage(pageNum,pageSize);
        List<Question> questionList = questionMapper.selectByExample(example);

        List<QuestionDTO> questionDTOList = new ArrayList<>();
        for (Question question : questionList) {
            User user = userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        return questionDTOList;
    }

    public Long waitCount() {
        QuestionExample example = new QuestionExample();
        example.createCriteria()
                .andCommentCountEqualTo(0)
                .andShieldEqualTo(0)
                .andExamineEqualTo(1);
        long total = questionMapper.countByExample(example);
        return total;
    }
}
