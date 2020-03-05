package com.qingfengzhuyue.forum.mapper;

import com.qingfengzhuyue.forum.model.Question;

public interface QuestionExtMapper {
    int incView(Question record);
    int incLike(Question record);
    int redLike(Question record);
    int incCommentCount(Question record);
}