package com.obtk.test;

import com.obtk.utils.QRcodeUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpSession;
import java.text.DateFormat;
import java.util.*;
import java.text.SimpleDateFormat;

public class Test {
    @org.junit.Test
    public void test(){
        ApplicationContext context=new ClassPathXmlApplicationContext("applicationContext.xml");
        RestTemplate restTemplate=context.getBean("restTemplate",RestTemplate.class);
        //向微信小程序后端API发换取openid的请求
        String res=restTemplate.getForObject("https://api.weixin.qq.com/sns/jscode2session?appid=wx76df30d67bc6edea&secret=44223730e5092d86848a88ed2815ba6c&grant_type=authorization_code&js_code=033W4v000ocMIL1D40200PgNlg1W4v0A",String.class);
        String[] split = res.split(":");
        String[] split1 = split[1].split(",");
        String sessionkey=split1[0].substring(1,split1[0].length()-1);
        System.out.println(sessionkey);
        String str=split[split.length-1];
        String s = str.substring(1, str.length() - 2);
        System.out.println(s);
        System.out.println(res);
    }
    @org.junit.Test
    public void test2(){
        String s = QRcodeUtils.tomakeMode("测试二维码", "e:/ic","test");
        System.out.println(s);
        //删除源文件
//        File  file = new File("C:/Users/Administrator/Desktop/1.jpg");
        // 路径为文件且不为空则进行删除
//        if (file.isFile() && file.exists()) {
//            file.delete();
//        }
    }

    @org.junit.Test
    public void test3(){
        DateFormat dateFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//格式化一下时间

        Date dNow = new Date(); //当前时间

        Date dBefore = new Date();

        Calendar calendar = Calendar.getInstance(); //得到日历

        calendar.setTime(dNow);//把当前时间赋给日历

        calendar.add(Calendar.DAY_OF_MONTH, -1); //设置为前一天

        dBefore = calendar.getTime(); //得到前一天的时间

        String defaultStartDate = dateFmt.format(dBefore); //格式化前一天

        defaultStartDate = defaultStartDate.substring(0,10)+" 00:00:00";

        String defaultEndDate = defaultStartDate.substring(0,10)+" 23:59:59";



        System.out.println(defaultStartDate);

        System.out.println(defaultEndDate);
    }


    @org.junit.Test
    public void test4(){
        for(int i=0;i<24;i=i+4){
            System.out.println(i);
        }
    }

    @org.junit.Test
    public void test5(){
        DateFormat dateFmt = new SimpleDateFormat("yyyy-MM-dd");//格式化一下时间
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        String start=dateFmt.format(calendar.getTime());

        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        String end=dateFmt.format(calendar.getTime());
        System.out.println(start);
        System.out.println(end);

        Date d1 = dateToWeek(new Date()).get(0);
        Date d2 = dateToWeek(new Date()).get(6);
        System.out.println(dateFmt.format(d1));
        System.out.println(dateFmt.format(d2));
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
