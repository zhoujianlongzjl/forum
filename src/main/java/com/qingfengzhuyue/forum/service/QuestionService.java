package com.qingfengzhuyue.forum.service;

import com.github.pagehelper.PageHelper;
import com.qingfengzhuyue.forum.dto.QuestionDTO;
import com.qingfengzhuyue.forum.mapper.QuestionExtMapper;
import com.qingfengzhuyue.forum.mapper.QuestionMapper;
import com.qingfengzhuyue.forum.mapper.UserMapper;
import com.qingfengzhuyue.forum.model.Question;
import com.qingfengzhuyue.forum.model.QuestionExample;
import com.qingfengzhuyue.forum.model.User;
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
    private UserMapper userMapper;

    public void createOrUpdate(Question question) {
        if (question.getId() == null) {
            //创建
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtCreate());
            question.setCommentCount(0);
            question.setViewCount(0);
            question.setLikeCount(0);
            questionMapper.insert(question);
        } else {
            //更新

            Question updateQuestion = new Question();
            updateQuestion.setGmtModified(System.currentTimeMillis());
            updateQuestion.setTitle(question.getTitle());
            updateQuestion.setDescription(question.getDescription());
            updateQuestion.setTag(question.getTag());

            QuestionExample example = new QuestionExample();
            example.createCriteria()
                    .andIdEqualTo(question.getId());
            questionMapper.updateByExampleSelective(updateQuestion, example);
        }
    }

    public List<QuestionDTO> list(Integer pageNum, Integer pageSize) {
        QuestionExample example = new QuestionExample();
        example.createCriteria()
                .andIdGreaterThanOrEqualTo(0L);
        example.setOrderByClause("gmt_create desc");
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

    public Long countQuestion() {
        QuestionExample example = new QuestionExample();
        example.createCriteria()
                .andIdGreaterThanOrEqualTo(0L);
        long total = questionMapper.countByExample(example);
        return total;
    }

    public QuestionDTO findById(Long id) {
        Question question = questionMapper.selectByPrimaryKey(id);
        User user = userMapper.selectByPrimaryKey(question.getCreator());
        QuestionDTO questionDTO = new QuestionDTO();
        BeanUtils.copyProperties(question, questionDTO);
        questionDTO.setUser(user);
        return questionDTO;
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
}
