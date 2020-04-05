package com.qingfengzhuyue.forum.scheduled;

import com.qingfengzhuyue.forum.cache.PopularQuestionCache;
import com.qingfengzhuyue.forum.dto.PopularQuestionDTO;
import com.qingfengzhuyue.forum.dto.QuestionDTO;
import com.qingfengzhuyue.forum.service.QuestionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Component
public class ScheduledService {

    @Autowired
    private QuestionService questionService;

    // 自动计算热度
    @Scheduled(cron = "0 0/30 9-17 * * *") // 朝九晚五工作时间内每半小时
    // @Scheduled(cron = "0/5 * * * * *")
    public void scheduled() {
        Integer pageNum = 1;
        Integer pageSize = 10;
        Integer userType = 0;
        List<QuestionDTO> list = new ArrayList<>();
        Long total = questionService.countQuestion(userType);
        List<PopularQuestionDTO> popularQuestionDTOS = new ArrayList<>();
        while (pageNum <= ((total / pageSize) + 1)) {
            list = questionService.list(pageNum, pageSize, userType);
            for (QuestionDTO questionDTO : list) {
                PopularQuestionDTO popularQuestionDTO = new PopularQuestionDTO();
                BeanUtils.copyProperties(questionDTO, popularQuestionDTO);
                // 计算热度
                Integer degreeOfHeat = questionDTO.getViewCount() + 5 * questionDTO.getCommentCount() + 4 * questionDTO.getLikeCount();
                popularQuestionDTO.setDegreeOfHeat(degreeOfHeat);
                popularQuestionDTOS.add(popularQuestionDTO);
            }
            pageNum += 1;
        }
        // 更据热度排序
        Collections.sort(popularQuestionDTOS);
        PopularQuestionCache.setPopularQuestionDTOS(popularQuestionDTOS);

        log.info("=====>>>>>使用cron  {}", System.currentTimeMillis());
    }
}
