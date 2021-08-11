package com.obtk.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.obtk.domain.Advert;
import com.obtk.domain.UserInfo;
import com.obtk.mapper.AdvertMapper;
import com.obtk.mapper.AdvertUserMapper;
import com.obtk.mapper.UserInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED,readOnly = true)
public class AdvertService {
    @Autowired
    private AdvertMapper advertMapper;
    @Autowired
    private AdvertUserMapper advertUserMapper;
    @Autowired
    private UserInfoMapper userInfoMapper;

    public PageInfo findByPage(int pageNo){
        //设置分页查询的起始行，和行数
        PageHelper.startPage(pageNo, 3);
        List<Advert> all = advertMapper.findAll();
        PageInfo pageInfo=new PageInfo(all);
        return pageInfo;
    }

    public List<Advert> findLike(String adName){
        return advertMapper.findLike(adName);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Map<String,Object> adOnline(int adid, String adlc){
        Map<String,Object> map=new HashMap<>();
        Advert adlc1 = advertMapper.getAdlc(adlc);

        if(!Objects.isNull(adlc1)){
            //该广告位已有广告上线
            map.put("msg","该广告位已有广告上线请重新选择。");
        }else {
            int i=advertMapper.adOnline(adid,adlc);
            map.put("suc",i);
        }
        return map;
    }

    public Advert getAdone(int adlc){
        return advertMapper.getAdlc(adlc+"");
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public int adUnder(int adid){
        return advertMapper.adUnder(adid);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public int updAdNum(int adid,int userid,int mark){
        int i=0;
        if(mark==1){
            //展示量
            i= advertMapper.UpdAdShow(adid);
        }else if(mark==2){
            //点击量
            i= advertMapper.UpdAdClick(adid)+advertUserMapper.addAdUser(userid,adid);
        }
        return i;
    }

    public Advert findOne(int adid){
        return advertMapper.findOne(adid);
    }

    public Map<String,Object> findByPageClick(int pageNo,int adid){
        System.out.println("广告id"+adid);
        Map<String,Object> map=new HashMap<>();
        int pageSize=2;
        //查询到所有点击用户id
        List<Integer> list = advertUserMapper.getUserInfo(adid);
        //所有用户集合
        List<UserInfo> all=new ArrayList<>();
        for (int i=0;i<list.size();i++){
            UserInfo one = userInfoMapper.findOne(list.get(i));
            all.add(one);
        }
        System.out.println(all);
        //总共页号
        int pageNum=0;
        if((all.size())%pageSize==0){
            pageNum=(all.size())/pageSize;
        }else {
            pageNum=(all.size())/pageSize+1;
        }
        //指定页号页数据
        List<UserInfo> oneList=new ArrayList<>();
        int start=(pageNo-1)*pageSize;
        //需要多少个数据
        System.out.println(pageNo+"页号");
        System.out.println(pageNum+"页数");
        if(pageNo!=pageNum){
            //不是最后一页数据
            for (int i=0;i<pageSize;i++){
                oneList.add(all.get(start));
                start++;
            }
        }else{
            System.out.println(adid+"几号广告");
            while (start<all.size()){
                oneList.add(all.get(start));
                System.out.println("单个："+all.get(start));
                start++;
            }

        }
        System.out.println("返回点击用户集合："+oneList);
        map.put("pageNum",pageNum);
        map.put("list",oneList);
        return map;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public int addAndUpd(int mark,int adid,String adName,String adType,String adUrl,String adSf){
        Advert advert=new Advert();
        advert.setAd_id(adid);
        advert.setAdvertName(adName);
        advert.setAdvertType(adType);
        advert.setAdvertUrl(adUrl);
        advert.setSpare2(adSf);
        int n=0;
        if(mark==1){
            //进行添加
            n = advertMapper.addOne(advert);
        }else {
            n = advertMapper.updOne(advert);
        }
        return n;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public int delOne(int adid){
        return advertMapper.delOne(adid);
    }

    public Map<String,Object> getAdProfit(){
        //获取一周盈利外，获取总展示量，总点击量，以便算总点击率
        Map<String,Object> map=new HashMap<>();
        // 获取一周前的今天
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date currentDate =new Date();
        Date d=new Date();
        d.setYear(currentDate.getYear());
        d.setMonth(currentDate.getMonth());
        d.setDate(currentDate.getDate()-7);
        //获取当前一周的日期
        List<Date> dates=new ArrayList<>();
        try{
//         dates= dateToWeek(sdf.parse("2021-06-12"));
         dates= dateToWeek(new Date());
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        for (int i=0;i<dates.size();i++){
            Date end=new Date();
            Date start=new Date();
            if(i==dates.size()-1){
                //周天
                end.setYear(dates.get(i).getYear());
                end.setMonth(dates.get(i).getMonth());
                end.setDate(dates.get(i).getDate()+1);
                start=dates.get(i);
            }else {
                //周一到周六
                start=dates.get(i);
                end=dates.get(i+1);
            }
            System.out.println(sdf.format(start));
            System.out.println(sdf.format(end));
            System.out.println("===========");
            //每天盈利集合，即点击、展示量集合
            List<Advert> profit = advertMapper.adProfit(sdf.format(start), sdf.format(end));
            if(profit.size()==0){
                map.put("profit"+i,0);
                map.put("click"+i,0);
                map.put("show"+i,0);
            }else {
                double dd=0;
                int cc=0;
                int ss=0;
                for(Advert a:profit){
                    int c=a.getClickNum();
                    int s=a.getShowNum();
                    if(c==0){
                        //判断点击量是否为0
                        cc+=0;
                    }else {
                        cc+=c;
                    }
                    if(s==0){
                        //判断展示量为0
                        ss+=0;
                    }else {
                        ss+=s;
                    }
                    if(c==0){
                        //判断是否有盈利
                        dd+=0;
                    }else {
                        double sf=Double.parseDouble(a.getSpare2());
                        dd+=c*sf;
                    }
                }
                //一天天的总点击量，总展示量，总盈利
                map.put("profit"+i,dd);
                map.put("click"+i,cc);
                map.put("show"+i,ss);
            }

        }
        System.out.println(map);
        return map;
    }

    public Map<String,Object> getOneDay(int adid){
        Map<String,Object> map=new HashMap<>();
        //根据获取前一天点击率
        DateFormat dateFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//格式化一下时间
        Date dNow=new Date();
        try {
             dNow= dateFmt.parse("2021-06-10 00:00:00"); //当前时间
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        Date dBefore = new Date();
        Calendar calendar = Calendar.getInstance(); //得到日历
        calendar.setTime(new Date());//把当前时间赋给日历
//        calendar.add(Calendar.DAY_OF_MONTH, -1); //设置为前一天
        dBefore = calendar.getTime(); //得到前一天的时间
        String dstart = dateFmt.format(dBefore); //格式化前一天
        dstart = dstart.substring(0,10)+" 00:00:00";
        String dend = dstart.substring(0,10)+" 23:59:59";
        String d1=dstart.substring(0,10);

        String start="";
        String end="";
        for(int i=0;i<24;i=i+4){
            if(i<10){
                start=d1+" 0"+i+":00:00";
                end=d1+" 0"+(i+4)+":00:00";
                if(i==8){
                    end=d1+" "+(i+4)+":00:00";
                }
            }else {
                start=d1+" "+i+":00:00";
                end=d1+" "+(i+4)+":00:00";
                if(i==20){
                    end=d1+" 23:59:59";
                }
            }

            int num = advertUserMapper.getClickNum(adid, dstart, dend, start, end);
            map.put("one"+i,num);
        }

        return map;
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
