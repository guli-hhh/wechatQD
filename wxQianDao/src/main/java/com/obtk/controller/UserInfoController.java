package com.obtk.controller;

import com.obtk.domain.UserInfo;
import com.obtk.service.ParticipantService;
import com.obtk.service.SendEmailService;
import com.obtk.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/user")
public class UserInfoController {
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private SendEmailService sendEmailService;

    @GetMapping("/change/{code}")
    public ResponseEntity<?> change(@PathVariable String code) throws IOException {
        return ResponseEntity.ok(userInfoService.change(code));
    }

    //添加单个
    @PostMapping("/addUserInfo")
    public ResponseEntity<?> addUserInfo(@RequestBody UserInfo userInfo){
        return ResponseEntity.ok(userInfoService.addOne(userInfo));
    }

    //查询所有
    @GetMapping("/findByPage/{pageNo}")
    public ResponseEntity<?> findByPage(@PathVariable int pageNo){
        return ResponseEntity.ok(userInfoService.findByPage(pageNo));
    }

    //模糊查询
    @GetMapping("/findLike/{nickName}")
    public ResponseEntity<?> findLike(@PathVariable String nickName){
        return ResponseEntity.ok(userInfoService.findLike(nickName));
    }

    //导出数据
    @GetMapping("/updata")
    public ResponseEntity<?> updata(@RequestParam int proid,@RequestParam int mark,@RequestParam String to){
        System.out.println(proid);
        System.out.println(mark);
        System.out.println(to);
        sendEmailService.send(to,proid,mark);
        return ResponseEntity.ok(1);
    }

    //根据id查询单个
    @GetMapping("/findOne/{userid}")
    public ResponseEntity<?> findOne(@PathVariable int userid){
        return ResponseEntity.ok(userInfoService.findOne(userid));
    }

    //根据id更改登录次数和最近登录时间,并且判断是否为禁用用户
    @PutMapping("/updUser/{userid}")
    public ResponseEntity<?> updUser(@PathVariable int userid){
        return ResponseEntity.ok(userInfoService.updUser(userid));
    }

    //获取用户统计数据
    @GetMapping("/getUserData")
    public ResponseEntity<?> getUserData(){
        return ResponseEntity.ok(userInfoService.getUserData());
    }

    //禁用获取启用用户
    @PatchMapping("/forbidden/{userid}/{mark}")
    public ResponseEntity<?> forbidden(@PathVariable int userid,@PathVariable int mark){
        return ResponseEntity.ok(userInfoService.forbidden(userid,mark));
    }

    //标记或取消用户
    @PatchMapping("/mark/{userid}/{mark}")
    public ResponseEntity<?> mark(@PathVariable int userid,@PathVariable String mark){
        return ResponseEntity.ok(userInfoService.mark(userid,mark));
    }
}
