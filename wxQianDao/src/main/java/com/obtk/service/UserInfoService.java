package com.obtk.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.obtk.domain.Uchange;
import com.obtk.domain.UserInfo;
import com.obtk.mapper.UchangeMapper;
import com.obtk.mapper.UserInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED,readOnly = true)
public class UserInfoService {
    @Autowired
    private UserInfoMapper userInfoMapper;
    @Autowired
    private UchangeMapper uchangeMapper;
    @Autowired
    private RestTemplate restTemplate;

    @Transactional(propagation = Propagation.REQUIRED)
    public Map<String,Object> change(String code) throws IOException {
        Map<String,Object> map=new HashMap<>();
        //code换openid和sessionkey
        String res=restTemplate.getForObject("https://api.weixin.qq.com/sns/jscode2session?appid=wx76df30d67bc6edea&secret=44223730e5092d86848a88ed2815ba6c&grant_type=authorization_code&js_code="+code,String.class);
        //将json格式字符串转对象  jackson
        ObjectMapper objectMapper=new ObjectMapper();
        Map<String,Object> params=objectMapper.readValue(res,new TypeReference<Map<String,Object>>(){});
        String[] split = res.split(":");
        //sessionkey
        String sessionkey=(String) params.get("session_key");
        //openid
        String openid =(String) params.get("openid");

        int userId = uchangeMapper.findUserId(openid);
        if(userId>0){
            //说明查到了
            int userId1 = uchangeMapper.findUserId1(openid);
            map.put("userid",userId1);
            UserInfo one = userInfoMapper.findOne(userId1);
            if(!Objects.isNull(one)){
                //返回数据库数据
                map.put("userInfo",one);
                //不用再发post请求存数据
                map.put("flag",false);
            }else {
                map.put("flag",true);
            }

        }else {
            //没有查到进行存储
            //先存转换表
            Uchange uchange=new Uchange();
            uchange.setOpenId(openid);
            uchange.setSession_key(sessionkey);
            int i = uchangeMapper.addOne(uchange);
            //再通过post请求存userinfo表
            //总计得到userid
            int counts = uchangeMapper.findUserId1(openid);
            map.put("userid",counts);
            map.put("flag",true);
        }
        return map;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public int addOne(UserInfo userInfo){
        return userInfoMapper.addOne(userInfo);
    }

    public PageInfo findByPage(int pageNo){
        //设置分页查询的起始行，和行数
        PageHelper.startPage(pageNo, 3);
        List<UserInfo> all = userInfoMapper.findAll();
        PageInfo pageInfo=new PageInfo(all);
        return pageInfo;
    }

    public List<UserInfo> findLike(String nickName){
        return userInfoMapper.findLike(nickName);
    }

    public UserInfo findOne(int userid){
        UserInfo one = userInfoMapper.findOne(userid);
        DateFormat dateFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String s1=dateFmt.format(one.getRecentlyTime());
        String s2=dateFmt.format(one.getWarrantTime());
        one.setRecentlyStr(s1);
        one.setWarrantStr(s2);
        return one;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public int updUser(int userid){
        //根据id获取用户状态是否为禁用,禁用跳别的页面,不进行登录
        UserInfo one = userInfoMapper.findOne(userid);
        if(!Objects.isNull(one)){
            if("拉黑".equals(one.getSpare1())){
                return 0;
            }else {
                return userInfoMapper.updUser(userid);
            }
        }else {
            return -1;
        }
    }

    public Map<String,Object> getUserData(){
        //获取统计数据，周新增用户，月新增用户
        DateFormat dateFmt = new SimpleDateFormat("yyyy-MM-dd");//格式化一下时间
        Map<String,Object> map=new HashMap<>();
        //周
        List<Date> dates = dateToWeek(new Date());
        String s1=dateFmt.format(dates.get(0));
        String s2=dateFmt.format(dates.get(6));
        int one = userInfoMapper.getOWuser(s1, s2);
        map.put("week",one);
        //月
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        String start=dateFmt.format(calendar.getTime());
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        String end=dateFmt.format(calendar.getTime());
        int two = userInfoMapper.getOWuser(start, end);
        map.put("month",two);
        //总
        map.put("total",userInfoMapper.findAll().size());
        return map;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public int forbidden(int userid,int mark){
        String str="";
        if(mark==0){
            str="拉黑";
        }else if(mark==1){
            str="取消拉黑";
        }
        return userInfoMapper.forbidden(userid,str);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public int mark(int userid,String mark){
        return userInfoMapper.mark(userid,mark);
    }

    /**
     * 根据日期获得所在周的日期
     * @param mdate
     * @return
     */
    @SuppressWarnings("deprecation")
    private static List<Date> dateToWeek(Date mdate) {
        int b = mdate.getDay();
        Date fdate;
        List<Date> list = new ArrayList<>();
        Long fTime = mdate.getTime() - b * 24 * 3600000;
        for (int a = 1; a <= 7; a++) {
            fdate = new Date();
            fdate.setTime(fTime + (a * 24 * 3600000));
            list.add(a - 1, fdate);
        }
        return list;
    }

}
