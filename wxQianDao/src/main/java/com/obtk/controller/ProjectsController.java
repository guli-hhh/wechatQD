package com.obtk.controller;

import com.obtk.domain.Projects;
import com.obtk.service.ProjectsService;
import com.obtk.utils.QRcodeUtils;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/pro")
public class ProjectsController {
    @Autowired
    private ProjectsService projectsService;

    //根据用户id查询所有所创建的签到活动
    @GetMapping("/findAll/{userId}")
    public ResponseEntity<?> findAll(@PathVariable int userId){
        return ResponseEntity.ok(projectsService.findAll(userId));
    }

    //添加一个签到活动
    @PostMapping("/addOne")
    public ResponseEntity<?> addOne(@RequestBody Projects projects){
        return ResponseEntity.ok(projectsService.addOne(projects));
    }

    //页面加载刷新活动状态
    @PutMapping("/updStatus/{proid}/{status}")
    public ResponseEntity<?> updStatus(@PathVariable("proid") int proId, @PathVariable("status") int status){
        return ResponseEntity.ok(projectsService.updStatus(proId,status));
    }

    //根据活动id查询单个
    @GetMapping("/findById/{proid}")
    public ResponseEntity<?> findById(@PathVariable int proid){
        return ResponseEntity.ok(projectsService.findById(proid));
    }

    //根据活动邀请码id查询单个
    @GetMapping("/findByinId/{inId}")
    public ResponseEntity<?> findByinviteId(@PathVariable int inId){
        return ResponseEntity.ok(projectsService.findByinviteId(inId));
    }

    //根据活动id查询单个
    @GetMapping("/findOne/{proid}")
    public ResponseEntity<?> findOne(@PathVariable int proid){
        return ResponseEntity.ok(projectsService.findOne(proid));
    }

    //查询所有
    @GetMapping("/findByPage/{pageNo}")
    public ResponseEntity<?> findByPage(@PathVariable int pageNo){
        return ResponseEntity.ok(projectsService.findByPage(pageNo));
    }

    //模糊查询
    @GetMapping("/findLike/{projectName}")
    public ResponseEntity<?> findLike(@PathVariable String projectName){
        return ResponseEntity.ok(projectsService.findLike(projectName));
    }

    //根据id删除单个活动且数据备份
    @DeleteMapping("/delOne/{proid}/{userid}")
    public ResponseEntity<?> delOne(@PathVariable int proid,@PathVariable int userid){
        return ResponseEntity.ok(projectsService.delOne(proid,userid));
    }

    //根据活动id生成对应的活动二维码（即签到二维码）
    @RequestMapping("/qrcode/{proid}")
    public ResponseEntity<?> download(@PathVariable int proid,HttpSession session)throws IOException {
        //先服务器生成二维码图片,保存服务器，
        System.out.println(proid);
        //二维码图片名字
        String codename="pro"+proid;
        //生成二维码要存储的服务器路径
        String path = session.getServletContext().getRealPath("IqdQrcode");
        //判断服务端是否已经生成二维码，有则直接使用
        File f=new File(path,"/"+codename+".png");
        if(!f.exists()){
            //在服务器生成二维码图片，并返回图片名
            String s = QRcodeUtils.tomakeMode("您已完成当前时间所有活动签到" , path, codename);
            //再去下载到本地
            //在项目服务器实际路径下找到要下载指定文件名的文件
             f = new File(path,s);
        }
        //设置下载响应包的头数据
        HttpHeaders httpHeaders=new HttpHeaders();
        //设置内容类型为流数据
        httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        //设置文件附件的名称
        httpHeaders.setContentDispositionFormData("attachment",codename);
        //封装响应对象返回， org.apache.commons.io.FileUtils包下的输入输出流的工具包，将文件写入本地，  HttpStatus：响应状态码
        return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(f),httpHeaders,HttpStatus.OK);
        //返回一个二进制的文件字节流(此处返回一个图片文件二进制字节流)
    }

    //获取活动统计数据
    @GetMapping("/getProData")
    public ResponseEntity<?> getProData(){
        return ResponseEntity.ok(projectsService.getProData());
    }

    //获取活动的报名数据
    @GetMapping("/getProEnroll/{proid}/{pageNo}")
    public ResponseEntity<?> getProEnroll(@PathVariable int proid,@PathVariable int pageNo){
        return ResponseEntity.ok(projectsService.getProEnroll(proid,pageNo));
    }

    //获取活动的成功签到数据
    @GetMapping("/getProSign/{proid}/{pageNo}")
    public ResponseEntity<?> getProSign(@PathVariable int proid,@PathVariable int pageNo){
        return ResponseEntity.ok(projectsService.getProSign(proid,pageNo));
    }

}
