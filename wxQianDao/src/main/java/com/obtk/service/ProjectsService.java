package com.obtk.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.obtk.domain.Participant;
import com.obtk.domain.Projects;
import com.obtk.mapper.ParticipantMapper;
import com.obtk.mapper.ProjectsDataMapper;
import com.obtk.mapper.ProjectsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED,readOnly = true)
public class ProjectsService {
    @Autowired
    private ProjectsMapper projectsMapper;
    @Autowired
    private ProjectsDataMapper projectsDataMapper;
    @Autowired
    private ParticipantMapper participantMapper;

    public List<Projects> findAll(int userId){
        return projectsMapper.findAll(userId);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public int addOne(Projects projects){
        //处理参与者需要提供的数据
        if(projects.getDiy1().equals("")){
            projects.setDiy1(null);
        }
        if(projects.getDiy2().equals("")){
            projects.setDiy2(null);
        }
        //随机生成邀请码
        Random rand=new Random();
        String str="";
        for (int i=0;i<6;i++){
            int j = rand.nextInt(10);
            str+=j;
        }
        projects.setInviteId(Integer.parseInt(str));

        //设置创建时间
        long now=new java.util.Date().getTime();
        projects.setCreateTime(now);
        //判断活动状态
        if(projects.getStartTime()>now){
            projects.setStatus(0);
        }else if(projects.getStartTime()<=now&&now<=projects.getEndTime()){
            projects.setStatus(1);
        }else if(now>projects.getEndTime()){
            projects.setStatus(2);
        }

        projects.setEnroll(0);
        projects.setFinish(0);
        return projectsMapper.addOne(projects);
    }

    //改定时器实现
    @Transactional(propagation = Propagation.REQUIRED)
    public int updStatus(int proId,int status){
        return projectsMapper.updStauts(proId,status);
    }
    //定时器
    public List<Projects> findAllTimer(long zero,long torzero){
        return projectsMapper.findAllTimer(zero,torzero);
    }

    public Projects findById(int proid){
        return projectsMapper.findById(proid);
    }

    public Projects findByinviteId(int inId){
        return projectsMapper.findByinviteId(inId);
    }

    public Projects findOne(int proid){
        return projectsMapper.findOne(proid);
    }

    public PageInfo findByPage(int pageNo){
        //设置分页查询的起始行，和行数
        PageHelper.startPage(pageNo, 3);
        List<Projects> all = projectsMapper.findByPage();
        PageInfo pageInfo=new PageInfo(all);
        return pageInfo;
    }

    public List<Projects> findLike(String projectName){
        return projectsMapper.findLike(projectName);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public int delOne(int proid,int userid){
        Projects one = projectsMapper.findOne(proid);
        int i = projectsDataMapper.addOne(one);
        int j = projectsMapper.delOne(proid);
        int k = participantMapper.delOneBypuid(proid, userid);
        return i+j+k;
    }

    public Map<String,Object> getProData(){
        Map<String,Object> map=new HashMap<>();
        //获取第一创建的活动的毫秒值
        long minCreateTime = projectsMapper.getMinCreateTime();
        //获取第一个创建的活动的零点毫秒数
        long zero = minCreateTime/(1000*3600*24)*(1000*3600*24) - TimeZone.getDefault().getRawOffset();
        System.out.println(zero+"最早活动时间");
        //当前时间毫秒数
        long current = System.currentTimeMillis();
        //当天零点毫秒数
        long now = current/(1000*3600*24)*(1000*3600*24) - TimeZone.getDefault().getRawOffset();
        System.out.println(now+"当前时间");
        System.out.println(now-zero+"差值");
        //判断截止当天经过多少天
        int n=(int)(now-zero)/(24*60*60*1000);
        int n1=(int)(zero-now)/(24*60*60*1000);
        System.out.println(n+"天数");
        System.out.println(n1+"天数");
        map.put("days",n1);

        //获取所有活动总数
        int totalPro = projectsMapper.getTotalPro();
        map.put("pros",totalPro);

        //获取参与总人数
        int totalPpt = participantMapper.getTotalPpt();
        map.put("pjoins",totalPpt);

        //获取签到总人数
        int totalSign = participantMapper.getTotalSign();
        map.put("signs",totalSign);
        return map;
    }

    public PageInfo getProEnroll(int proid,int pageNo){
        //设置分页查询的起始行，和行数
        PageHelper.startPage(pageNo, 2);
        List<Participant> all = participantMapper.findByproid(proid);
        PageInfo pageInfo=new PageInfo(all);
        return pageInfo;
    }

    public PageInfo getProSign(int proid,int pageNo){
        //设置分页查询的起始行，和行数
        PageHelper.startPage(pageNo, 2);
        List<Participant> all = participantMapper.findUn(proid,1);
        PageInfo pageInfo=new PageInfo(all);
        return pageInfo;
    }
}
