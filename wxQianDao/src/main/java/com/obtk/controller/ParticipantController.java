package com.obtk.controller;

import com.obtk.domain.Participant;
import com.obtk.domain.SignUtils;
import com.obtk.service.ParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ppt")
public class ParticipantController {
    @Autowired
    private ParticipantService participantService;

    //根据userid查询所有参与活动
    @GetMapping("/findAll/{userId}")
    public ResponseEntity<?> findAll(@PathVariable int userId){
        return ResponseEntity.ok(participantService.findAll(userId));
    }

    //删除和数据备份
    @DeleteMapping("/delAndBack/{pid}")
    public ResponseEntity<?> delAndBack(@PathVariable int pid){
        return ResponseEntity.ok(participantService.delAndBack(pid));
    }

    //添加参与者数据
    @PostMapping("/addOne")
    public ResponseEntity<?> addOne(@RequestBody Participant participant){
        return ResponseEntity.ok(participantService.addOne(participant));
    }

    //进行签到
    @PutMapping("/sign")
    public ResponseEntity<?> sign(@RequestBody SignUtils signUtils){
        System.out.println(signUtils);
        return ResponseEntity.ok(participantService.sign(signUtils));
    }

    //根据活动id查询所有参与者
    @GetMapping("/findByproid/{proid}/{mark}")
    public ResponseEntity<?> findByproid(@PathVariable int proid,@PathVariable int mark){
        List<Participant> byproid = participantService.findByproid(proid, mark);
        return ResponseEntity.ok(byproid);
    }
}
