package com.obtk.controller;

import com.obtk.domain.UserInfo;
import com.obtk.service.AdvertService;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/advert")
public class AdvertController {
    @Autowired
    private AdvertService advertService;

    //查询所有
    @GetMapping("/findByPage/{pageNo}")
    public ResponseEntity<?> findByPage(@PathVariable int pageNo){
        return ResponseEntity.ok(advertService.findByPage(pageNo));
    }

    //模糊查询
    @GetMapping("/findLike/{adName}")
    public ResponseEntity<?> findLike(@PathVariable String adName){
        return ResponseEntity.ok(advertService.findLike(adName));
    }

    //广告上线操作
    @PatchMapping("/adOnline/{adid}/{adlc}")
    public ResponseEntity<?> adOnline(@PathVariable("adid") String adid,@PathVariable("adlc") String adlc){
        return ResponseEntity.ok(advertService.adOnline(Integer.parseInt(adid),adlc));
    }

    //根据广告位获取广告详情
    @GetMapping("/getAdone/{adlc}")
    public ResponseEntity<?> getAdone(@PathVariable int adlc){
        return ResponseEntity.ok(advertService.getAdone(adlc));
    }

    //广告下线操作
    @PatchMapping("/adUnder/{adid}")
    public ResponseEntity<?> adOnline(@PathVariable("adid") int adid){
        System.out.println(adid);
        return ResponseEntity.ok(advertService.adUnder(adid));
    }

    //广告上线操作
    @PutMapping("/updAdNum/{adid}/{userid}/{mark}")
    public ResponseEntity<?> updAdNum(@PathVariable("adid") int adid,@PathVariable("userid") int userid,@PathVariable("mark") int mark){
        return ResponseEntity.ok(advertService.updAdNum(adid,userid,mark));
    }

    //根据id查询单个提供统计数据、编辑广告
    @GetMapping("/findOne/{adid}")
    public ResponseEntity<?> findOne(@PathVariable("adid") int adid){
        return ResponseEntity.ok(advertService.findOne(adid));
    }

    //根据id查询单个广告点击用户
    @GetMapping("/findByClick/{pageNo}/{adid}")
    public ResponseEntity<?> findByClick(@PathVariable("pageNo") int pageNo,@PathVariable("adid") int adid){
        return ResponseEntity.ok(advertService.findByPageClick(pageNo,adid));
    }

    //添加广告/编辑广告
    @PostMapping("/addAd/{mark}/{adid}")
    public ResponseEntity<?> addAd(@PathVariable int mark,@PathVariable int adid,@RequestParam String adName,@RequestParam String adType,@RequestParam String adUrl,@RequestParam String adSf){
        return ResponseEntity.ok(advertService.addAndUpd(mark,adid,adName,adType,adUrl,adSf));
    }

    //删除广告
    @DeleteMapping("/delOne/{adid}")
    public ResponseEntity<?> delOne(@PathVariable int adid){
        return ResponseEntity.ok(advertService.delOne(adid));
    }

    //上周广告盈利
    @GetMapping("/getAdProfit")
    public ResponseEntity<?> getAdProfit(){
        return ResponseEntity.ok(advertService.getAdProfit());
    }

    //广告前一天的点击率
    @GetMapping("/getOneDay/{adid}")
    public ResponseEntity<?> getOneDay(@PathVariable int adid){
        return ResponseEntity.ok(advertService.getOneDay(adid));
    }
}
