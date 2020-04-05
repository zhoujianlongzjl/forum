package com.qingfengzhuyue.forum.service;

import com.github.pagehelper.PageHelper;
import com.qingfengzhuyue.forum.mapper.TagMapper;
import com.qingfengzhuyue.forum.model.Tag;
import com.qingfengzhuyue.forum.model.TagExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagService {

    @Autowired
    private TagMapper tagMapper;

    public List<Tag> findAll() {
        TagExample example = new TagExample();
        example.createCriteria()
                .andIdGreaterThanOrEqualTo(0L);
        example.setOrderByClause("gmt_create desc");
        List<Tag> tags = tagMapper.selectByExample(example);
        return tags;
    }

    public List<Tag> findName(String name){
        TagExample example = new TagExample();
        example.createCriteria()
                .andNameEqualTo(name);
        List<Tag> tags = tagMapper.selectByExample(example);
        return tags;
    }

    public void addTag(Tag tag) {
        tag.setGmtCreate(System.currentTimeMillis());
        tag.setGmtModified(tag.getGmtCreate());
        tagMapper.insert(tag);
    }

    public Long countTag() {
        TagExample example = new TagExample();
        example.createCriteria()
                .andIdGreaterThanOrEqualTo(0L);
        long total = tagMapper.countByExample(example);
        return total;
    }

    public void deleteTag(Long tagId) {
        tagMapper.deleteByPrimaryKey(tagId);
    }

    public Tag findById(Long id) {
        Tag tag = tagMapper.selectByPrimaryKey(id);
        return tag;
    }

    public List<Tag> findTagPage(Integer pageNum, Integer pageSize) {
        TagExample example = new TagExample();
        example.createCriteria()
                .andIdGreaterThanOrEqualTo(0L);
        PageHelper.startPage(pageNum,pageSize);
        List<Tag> tags = tagMapper.selectByExample(example);
        return tags;
    }
}
