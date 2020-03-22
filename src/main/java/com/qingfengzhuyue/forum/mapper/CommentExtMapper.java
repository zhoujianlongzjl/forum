package com.qingfengzhuyue.forum.mapper;

import com.qingfengzhuyue.forum.model.Comment;

public interface CommentExtMapper {
    int incCommentCount(Comment comment);
}