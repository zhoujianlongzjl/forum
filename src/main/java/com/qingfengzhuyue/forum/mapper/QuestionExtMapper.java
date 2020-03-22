package com.qingfengzhuyue.forum.mapper;

import com.qingfengzhuyue.forum.dto.QuestionQueryDTO;
import com.qingfengzhuyue.forum.model.Question;

import java.util.List;

public interface QuestionExtMapper {
    int incView(Question record);
    int incLike(Question record);
    int redLike(Question record);
    int incCommentCount(Question record);

    Integer countBySearch(QuestionQueryDTO questionQueryDTO);
    List<Question> selectBySearch(QuestionQueryDTO questionQueryDTO);
}