package com.qingfengzhuyue.forum.dto;

import com.qingfengzhuyue.forum.model.User;
import lombok.Data;

@Data
public class PopularQuestionDTO implements Comparable<PopularQuestionDTO> {
    private Long id;
    private String title;
    private String description;
    private String tag;
    private Long gmtCreate;
    private Long gmtModified;
    private Long creator;
    private Integer viewCount;
    private Integer commentCount;
    private Integer likeCount;
    private Integer shield;
    private Integer examine;
    private User user;
    private Integer degreeOfHeat;

    @Override
    public int compareTo(PopularQuestionDTO o) {
        int i = o.getDegreeOfHeat() - this.getDegreeOfHeat();//先按照热度排序(降序)
        if (i == 0) {
            return (int) (this.getGmtCreate() - o.getGmtCreate());//如果热度相等了再用创建时间进行排序(降序)
        }
        return i;
    }
}
