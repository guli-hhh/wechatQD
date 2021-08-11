package com.obtk.service;

import com.obtk.domain.Participant;
import com.obtk.mapper.ParticipantMapper;
import com.sun.mail.util.MailSSLSocketFactory;
import org.apache.poi.hssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.*;
import java.util.*;

@Service
public class SendEmailService {

    @Autowired
    private ParticipantMapper participantMapper;

    //导出数据准备
    private List<Participant> findupdata(int proid, int mark){
        if(mark==0){
            //获取报名数据
            return participantMapper.findByproid(proid);
        }else {
            //获取签到数据
            return participantMapper.findUn(proid,1);
        }
    }

    //存储数据到本地用于邮件发送的附件
    //Excel表的创建
    private HSSFWorkbook createExcel(int proid, int mark){
        //获取数据库数据
        List<Participant> list = findupdata(proid, mark);
        //excel文件名字
        String tableName="";
        //字段集合
        List<String> columns =new ArrayList<>(5);
        //判断字段是否为空
        int a=0;
        if(!Objects.isNull(list.get(0))){
            System.out.println("有数据。");
            if(!Objects.isNull(list.get(0).getPname())){
                System.out.println("名字不为空");
                columns.add(0,"姓名");
                a=1;
            }
            if(!Objects.isNull(list.get(0).getPhone())){
                System.out.println("电话不为空");
                columns.add(1,"电话");
                a=2;
            }
            if(!Objects.isNull(list.get(0).getDiy3())){
                System.out.println("三列不为空");
                columns.add(2,"三列");
                a=3;
            }
            if(!Objects.isNull(list.get(0).getDiy4())){
                System.out.println("四列不为空");
                columns.add(3,"四列");
                a=4;
            }
            if(!Objects.isNull(list.get(0).getDiy5())){
                System.out.println("五列不为空");
                columns.add(4,"五列");
                a=5;
            }
        }
//        columns.add(3,"diy4");
//        columns.add(4,"diy5");
        if(mark==0){
            tableName="报名数据";
            columns.add(a,"签到状态");
        }else {
            tableName="签到数据";
        }
        //创建一个Excel表
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet(tableName);
        //设置单元格样式style（居中加粗自动换行）
        HSSFCellStyle style = workbook.createCellStyle();
        HSSFFont font = workbook.createFont();
        font.setBold(true);//加粗
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);//居中
        style.setWrapText(true);//自动换行
        style.setFont(font);
        //表头创建
        HSSFRow row = sheet.createRow(0);//表头行
        int columnLength = columns.size();//拥有的表列
        for (int i=0;i<columnLength;i++){
            sheet.setColumnWidth(i,15*256);//设置表头的列宽
            row.createCell(i).setCellValue(columns.get(i));
        }
        //将数据填充单元格
        int dataLength = list.size();

        for (int i=0;i<dataLength;i++){
            row = sheet.createRow(i+1);//第一行是表头 数据填充从第二行开始
            //设置单元格
            if(mark==0){
                //报名数据
                if(a==1){
                    row.createCell(0).setCellValue(list.get(i).getPname());
                }else if(a==2){
                    row.createCell(0).setCellValue(list.get(i).getPname());
                    row.createCell(1).setCellValue(list.get(i).getPhone());
                }else if(a==3){
                    row.createCell(0).setCellValue(list.get(i).getPname());
                    row.createCell(1).setCellValue(list.get(i).getPhone());
                    row.createCell(2).setCellValue(list.get(i).getDiy3());
                }else if(a==4){
                    row.createCell(0).setCellValue(list.get(i).getPname());
                    row.createCell(1).setCellValue(list.get(i).getPhone());
                    row.createCell(2).setCellValue(list.get(i).getDiy3());
                    row.createCell(3).setCellValue(list.get(i).getDiy4());
                }else if(a==5){
                    row.createCell(0).setCellValue(list.get(i).getPname());
                    row.createCell(1).setCellValue(list.get(i).getPhone());
                    row.createCell(2).setCellValue(list.get(i).getDiy3());
                    row.createCell(3).setCellValue(list.get(i).getDiy4());
                    row.createCell(4).setCellValue(list.get(i).getDiy5());
                }
                row.createCell(a).setCellValue(list.get(i).getStatus());
            }else {
                //签到数据
                if(columnLength==0){
                    row.createCell(0).setCellValue(list.get(i).getPname());
                }else if(columnLength==1){
                    row.createCell(0).setCellValue(list.get(i).getPname());
                    row.createCell(1).setCellValue(list.get(i).getPhone());
                }else if(columnLength==2){
                    row.createCell(0).setCellValue(list.get(i).getPname());
                    row.createCell(1).setCellValue(list.get(i).getPhone());
                    row.createCell(2).setCellValue(list.get(i).getDiy3());
                }else if(columnLength==3){
                    row.createCell(0).setCellValue(list.get(i).getPname());
                    row.createCell(1).setCellValue(list.get(i).getPhone());
                    row.createCell(2).setCellValue(list.get(i).getDiy3());
                    row.createCell(3).setCellValue(list.get(i).getDiy4());
                }else if(columnLength==4) {
                    row.createCell(0).setCellValue(list.get(i).getPname());
                    row.createCell(1).setCellValue(list.get(i).getPhone());
                    row.createCell(2).setCellValue(list.get(i).getDiy3());
                    row.createCell(3).setCellValue(list.get(i).getDiy4());
                    row.createCell(4).setCellValue(list.get(i).getDiy5());
                }
            }

        }
        return workbook;

    }

    //生成Excel且保存至服务器
    private String create(int proid, int mark){
        String excelName="";
        if(mark==0){
            excelName="报名数据";
        }else {
            excelName="签到数据";
        }
        try {
            File file = new File("E:\\IqD\\"+excelName+".xls");
            if(!file.exists()) {
                file.createNewFile(); // 创建新文件,有同名的文件的话直接覆盖
            }
            //写入服务器磁盘
            FileOutputStream out = new FileOutputStream(file);
            createExcel(proid,mark).write(out);
            return "成功";
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }


    //进行邮件发送
    public String send(String to,int proid,int mark) {
        //数据的准备
        create(proid, mark);

        // 收件人电子邮箱
        String toH = to;

        // 发件人电子邮箱
        String from = "2940188491@qq.com";

        // 获取系统属性
        Properties properties = new Properties();

        //发送邮件协议
        properties.setProperty("mail.transport.protocol", "SMTP");

        // 设置邮件服务器
        properties.setProperty("mail.host", "smtp.qq.com");

        // 设置邮件服务器端口
        properties.setProperty("mail.smtp.port", "465");

        try {

            //开启SSL加密：QQ邮箱需要
            MailSSLSocketFactory sf = new MailSSLSocketFactory();
            sf.setTrustAllHosts(true);
            properties.put("mail.smtp.ssl.enable", "true");
            properties.put("mail.smtp.ssl.socketFactory", sf);

            // 设置邮件服务器是否需要登录认证
            properties.setProperty("mail.smtp.auth", "true");


            // 验证账号及密码，密码需要是第三方授权码
            Authenticator auth = new Authenticator() {
                public PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication("2940188491@qq.com", "wfzdrpksydovdega");
                }
            };

            // 获取默认session对象
            Session session = Session.getInstance(properties, auth);

            // 创建默认的 MimeMessage 对象
            MimeMessage message = new MimeMessage(session);

            // Set From: 头部头字段
            message.setFrom(new InternetAddress(from));

            // Set To: 邮件接收人
            message.addRecipient(Message.RecipientType.TO,
                    new InternetAddress(toH));

            // Set Subject: 主题名称
            message.setSubject("I签到数据邮件");

            // 创建消息部分
            BodyPart messageBodyPart = new MimeBodyPart();

            // 消息内容
            String cont = "您好，附件里是您的签到";
            String filename = "";
            if (mark == 0) {
                cont += "报名数据，0表示未签到，1表示签到成功，2表示签到失败。";
                filename = "报名数据";
            } else {
                cont += "签到数据";
                filename = "签到数据";
            }
            cont += "若不能正常显示，是因为太长超出单元格宽度，拉宽即可。感谢使用I签到，如有疑问可进入小程序点击 我的-联系我们。";

            messageBodyPart.setText(cont);

            // 创建多重消息
            Multipart multipart = new MimeMultipart();

            // 设置文本消息部分
            multipart.addBodyPart(messageBodyPart);

            // 附件部分
            messageBodyPart = new MimeBodyPart();

            //把文件，添加到附件1中
            //数据源
            DataSource source = new FileDataSource(new File("E:/IqD/" + filename + ".xls"));
            //设置第一个附件的数据
            messageBodyPart.setDataHandler(new DataHandler(source));
            //设置附件的文件名
            messageBodyPart.setFileName("Iqd.xls");
            multipart.addBodyPart(messageBodyPart);
            message.setContent(multipart);

            // 发送消息
            Transport.send(message);
            System.out.println("成功");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return "成功";


    }

}
