package com.qingfengzhuyue.forum.service;

import com.github.pagehelper.PageHelper;
import com.qingfengzhuyue.forum.dto.QuestionQueryDTO;
import com.qingfengzhuyue.forum.mapper.QuestionExtMapper;
import com.qingfengzhuyue.forum.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchService {

    @Autowired
    private QuestionExtMapper questionExtMapper;
    public List<Question> selectBySearch(String search,Integer pageNum,Integer pageSize) {

        QuestionQueryDTO questionQueryDTO = new QuestionQueryDTO();
        questionQueryDTO.setSearch(search);
        PageHelper.startPage(pageNum, pageSize);
        List<Question> questionList = questionExtMapper.selectBySearch(questionQueryDTO);

        return questionList;
    }

    public Integer countBySearch(String search) {
        QuestionQueryDTO questionQueryDTO = new QuestionQueryDTO();
        questionQueryDTO.setSearch(search);
        Integer integer = questionExtMapper.countBySearch(questionQueryDTO);
        return integer;
    }
}
