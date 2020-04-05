package com.qingfengzhuyue.forum.service;

import com.github.pagehelper.PageHelper;
import com.qingfengzhuyue.forum.mapper.QuestionExtMapper;
import com.qingfengzhuyue.forum.mapper.QuestionMapper;
import com.qingfengzhuyue.forum.model.Question;
import com.qingfengzhuyue.forum.model.QuestionExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchService {

    @Autowired
    private QuestionMapper questionMapper;

    public List<Question> selectBySearch(String search, Integer pageNum, Integer pageSize, Integer userType, Integer searchType) {

        if (searchType == 0) {
            // 根据标题搜索
            if (userType != 0) {
                // 管理员查询
                QuestionExample example = new QuestionExample();
                QuestionExample.Criteria criteria = example.createCriteria();
                search = search.replace(' ', '%');
                search = "%" + search + "%";
                criteria.andTitleLike(search);
                example.setOrderByClause("gmt_create desc");
                PageHelper.startPage(pageNum, pageSize);
                List<Question> questionList = questionMapper.selectByExample(example);
                return questionList;
            } else {
                // 普通用户查询
                QuestionExample example = new QuestionExample();
                QuestionExample.Criteria criteria = example.createCriteria();
                search = search.replace(' ', '%');
                search = "%" + search + "%";
                criteria.andTitleLike(search)
                        .andExamineEqualTo(1)
                        .andShieldEqualTo(0);
                example.setOrderByClause("gmt_create desc");
                PageHelper.startPage(pageNum, pageSize);
                List<Question> questionList = questionMapper.selectByExample(example);
                return questionList;
            }
        } else {
            // 根据标签搜索
            if (userType != 0) {
                // 管理员查询
                QuestionExample example = new QuestionExample();
                QuestionExample.Criteria criteria = example.createCriteria();
                search = search.replace(' ', '%');
                search = "%" + search + "%";
                criteria.andTagLike(search);
                example.setOrderByClause("gmt_create desc");
                PageHelper.startPage(pageNum, pageSize);
                List<Question> questionList = questionMapper.selectByExample(example);
                return questionList;
            } else {
                // 普通用户查询
                QuestionExample example = new QuestionExample();
                QuestionExample.Criteria criteria = example.createCriteria();
                search = search.replace(' ', '%');
                search = "%" + search + "%";
                criteria.andTagLike(search)
                        .andExamineEqualTo(1)
                        .andShieldEqualTo(0);
                example.setOrderByClause("gmt_create desc");
                PageHelper.startPage(pageNum, pageSize);
                List<Question> questionList = questionMapper.selectByExample(example);
                return questionList;
            }
        }

    }

    public Long countBySearch(String search, Integer userType, Integer searchType) {

        if (searchType == 0) {
            // 根据标题搜索
            if (userType != 0) {
                // 管理员查询
                QuestionExample example = new QuestionExample();
                QuestionExample.Criteria criteria = example.createCriteria();
                search = search.replace(' ', '%');
                search = "%" + search + "%";
                criteria.andTitleLike(search);
                long integer = questionMapper.countByExample(example);
                return integer;
            } else {
                // 普通用户查询
                QuestionExample example = new QuestionExample();
                QuestionExample.Criteria criteria = example.createCriteria();
                search = search.replace(' ', '%');
                search = "%" + search + "%";
                criteria.andTitleLike(search)
                        .andExamineEqualTo(1)
                        .andShieldEqualTo(0);
                long integer = questionMapper.countByExample(example);
                return integer;
            }
        } else {
            // 根据标签搜索
            if (userType != 0) {
                // 管理员查询
                QuestionExample example = new QuestionExample();
                QuestionExample.Criteria criteria = example.createCriteria();
                search = search.replace(' ', '%');
                search = "%" + search + "%";
                criteria.andTagLike(search);
                long integer = questionMapper.countByExample(example);
                return integer;
            } else {
                // 普通用户查询
                QuestionExample example = new QuestionExample();
                QuestionExample.Criteria criteria = example.createCriteria();
                search = search.replace(' ', '%');
                search = "%" + search + "%";
                criteria.andTagLike(search)
                        .andExamineEqualTo(1)
                        .andShieldEqualTo(0);
                long integer = questionMapper.countByExample(example);
                return integer;
            }
        }
    }
}
