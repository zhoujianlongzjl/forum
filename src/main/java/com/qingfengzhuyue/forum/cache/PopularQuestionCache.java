package com.qingfengzhuyue.forum.cache;

import com.qingfengzhuyue.forum.dto.PopularQuestionDTO;
import com.qingfengzhuyue.forum.dto.TagDTO;
import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
public class PopularQuestionCache {

    private static List<PopularQuestionDTO> popularQuestionDTOS = new ArrayList<>();

    public static List<PopularQuestionDTO> getPopularQuestionDTOS() {
        return popularQuestionDTOS;
    }

    public static void setPopularQuestionDTOS(List<PopularQuestionDTO> popularQuestionDTOS) {
        PopularQuestionCache.popularQuestionDTOS = popularQuestionDTOS;
    }

    public static Boolean isEmpty(){
        return PopularQuestionCache.popularQuestionDTOS.isEmpty();
    }
}
