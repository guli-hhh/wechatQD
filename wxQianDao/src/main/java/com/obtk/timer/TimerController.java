package com.obtk.timer;

import com.obtk.domain.Projects;
import com.obtk.mapper.ProjectsMapper;
import com.obtk.service.ProjectsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.TimeZone;

@Component
public class TimerController {
    @Autowired
    private ProjectsService projectsService;

    @Scheduled(cron = "0 0/1 * * * ?")//每分钟执行一次
    public void excute()
    {
        //获取当天零点时间戳
        //当前时间毫秒数
        long current = System.currentTimeMillis();
        //当天零点毫秒数
        long zero = current/(1000*3600*24)*(1000*3600*24) - TimeZone.getDefault().getRawOffset();
        //明天零点毫秒数
        long torzero=zero+24*60*60*1000;

        System.out.println(zero);
        //获取系统当前时间
        long time = new Date().getTime();
        //获取所有活动id和结束时间
        List<Projects> list = projectsService.findAllTimer(zero,torzero);
        //遍历 并判断是否签到结束
        list.forEach(pro -> {
            int status=pro.getStatus();
            //到当前时间活动应该有的状态
            int num=-1;
            if(pro.getEndTime()>=time&&time>=pro.getStartTime()){
                //活动进行中
                num=1;
            }else if(time<pro.getStartTime()){
                //未开始
                num=0;
            }

            //判断存储的状态与当前应该有的状态是否一致  不一致则改
            if(!(num==status)) {
                projectsService.updStatus(pro.getPro_id(),num);
            }
        });
    }

}
