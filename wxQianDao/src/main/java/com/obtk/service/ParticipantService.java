package com.obtk.service;

import com.obtk.domain.Participant;
import com.obtk.domain.Projects;
import com.obtk.domain.SignUtils;
import com.obtk.mapper.ParticipantMapper;
import com.obtk.mapper.ParticipantDataMapper;
import com.obtk.mapper.ProjectsMapper;
import com.obtk.mapper.UserInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED,readOnly = true)
public class ParticipantService {
    @Autowired
    private ParticipantMapper participantMapper;
    @Autowired
    private ParticipantDataMapper participantDataMapper;
    @Autowired
    private ProjectsMapper projectsMapper;


    public List<Participant> findAll(int userId){
        return participantMapper.findAll(userId);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public int delAndBack(int pid){
        //先查询单个用于数据备份
        Participant one = participantMapper.findOne(pid);
        //再删除单个
        int i = participantMapper.delOne(pid);
        //最后数据备份
        int j = participantDataMapper.addOne(one);
        return i+j;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public int addOne(Participant participant){
        //判断是否已经报名
        Participant pone = participantMapper.findByuidAndproid(participant.getUserId(), participant.getPro_id());
        if(Objects.isNull(pone)){
            //添加一条参与者信息
            participantMapper.addOne(participant);
            //更改活动表的表明人数
            projectsMapper.updEnroll(participant.getPro_id());
            return 1;

        }else {
            return -2;
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Map<String,Object> sign(SignUtils signUtils){
        System.out.println(signUtils);
        Map<String,Object> map=new HashMap<>();
        //判断是否有未签到的活动
        List<Integer> list = participantMapper.findProidList(signUtils.getUserId());
        if(list.size()==0){
            //没有未签到的
            map.put("msg","您暂无签到活动。");
        }else {
            //有未签到的，则进行时间判断
            List<SignUtils> slist=new ArrayList<>();
            for (int i=0;i<list.size();i++){
              boolean flag=true;
              SignUtils su=new SignUtils();
              su.setNum(-1);
              int proid=list.get(i);
              //通过proid查询项目信息
              Projects pro = projectsMapper.findByIdAndStatus(proid);
              if(Objects.isNull(pro)){
                  continue;
              }
              //地理位置判断
              boolean f1 = pro.getPlace_name().equals(signUtils.getPlaceName());
              boolean f2 = pro.getPlace_address().equals(signUtils.getPlaceAdress());
              String strlat=pro.getLatitude()+"";
              String strlat1=signUtils.getLat()+"";
              boolean f3 = strlat.equals(strlat1);
              String strlng=pro.getLongitude()+"";
              String strlng1=signUtils.getLng()+"";
              boolean f4 = strlng.equals(strlng1);
              if(!f1&&!f2&&!f3&&!f4){
                  su.setMarkmsg("您未到指定位置。");
                  flag=false;
              }

              //修改参与者的签到状态
              if(flag){
                  int userid=signUtils.getUserId();
                  int j=participantMapper.updStatus(userid, proid);
                  if(j>0){
                      //修改活动表完成签到人数
                      int b = projectsMapper.updFinish(proid);
                      if(b>0){
                          su.setNum(1);
                      }
                  }
              }
              slist.add(su);
            }
            System.out.println(slist);
            map.put("list",slist);

        }

        return map;
    }

    public List<Participant> findByproid(int proid,int mark){
        if(mark==0){
            return participantMapper.findUn(proid,0);
        }else {
            return participantMapper.findByproid(proid);
        }
    }






}
