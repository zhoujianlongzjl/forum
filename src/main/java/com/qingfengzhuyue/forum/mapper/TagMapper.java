package com.qingfengzhuyue.forum.mapper;

import com.qingfengzhuyue.forum.model.Tag;
import com.qingfengzhuyue.forum.model.TagExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface TagMapper {
    long countByExample(TagExample example);

    int deleteByExample(TagExample example);

    int deleteByPrimaryKey(Long id);

    int insert(Tag record);

    int insertSelective(Tag record);

    List<Tag> selectByExampleWithRowbounds(TagExample example, RowBounds rowBounds);

    List<Tag> selectByExample(TagExample example);

    Tag selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") Tag record, @Param("example") TagExample example);

    int updateByExample(@Param("record") Tag record, @Param("example") TagExample example);

    int updateByPrimaryKeySelective(Tag record);

    int updateByPrimaryKey(Tag record);
}