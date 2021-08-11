package com.obtk.mapper;

import com.obtk.domain.Advert;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface AdvertMapper {
    //查询所有
    @Select("select * from advert")
    List<Advert> findAll();

    //根据用户昵称模糊查询
    @Select("SELECT * FROM advert WHERE advertName LIKE '%${advertName}%'")
    List<Advert> findLike(@Param("advertName") String advertName);

    //根据id进行上线操作
    @Update("update advert set status=1,onlineTime=now(),underTime=null,spare1=#{adlc} where ad_id=#{adid}")
    int adOnline(@Param("adid") int adid,@Param("adlc") String adlc);

    //根据id进行下线操作
    @Update("update advert set status=2,underTime=now(),spare1=null where ad_id=#{adid}")
    int adUnder(@Param("adid") int adid);

    //根据广告位参数，判断该广告是否已经有广告上线/获取指定广告位已上线的广告
    @Select("select * from advert where spare1=#{adlc} and status=1")
    Advert getAdlc(@Param("adlc") String adlc);

    //根据id增加单个广告展示量
    @Update("update advert set showNum=showNum+1 where  ad_id=#{adid}")
    int UpdAdShow(@Param("adid") int adid);

    //根据id增加单个广告展示量
    @Update("update advert set clickNum=clickNum+1 where  ad_id=#{adid}")
    int UpdAdClick(@Param("adid") int adid);

    //根据id查询单个（统计数据使用、修改广告使用）
    @Select("select * from advert where  ad_id=#{adid}")
    Advert findOne(@Param("adid") int adid);

    //添加单个广告
    @Insert("insert into advert values(null,#{advertName},#{advertType},#{advertUrl},0,null,null,0,0,null,#{spare2})")
    int addOne(Advert advert);

    //修改单个广告
    @Update("update advert set advertName=#{advertName},advertType=#{advertType},advertUrl=#{advertUrl},spare2=#{spare2} where  ad_id=#{ad_id}")
    int updOne(Advert advert);

    //删除单个广告
    @Delete("delete from advert where  ad_id=#{adid}")
    int delOne(@Param("adid") int adid);

    //统计上周一周广告盈利及一周的点击量
    @Select("SELECT showNum,clickNum,clickNum,spare2 FROM advert WHERE onlineTime BETWEEN #{start} AND #{end} ")
    List<Advert> adProfit(@Param("start") String start,@Param("end") String end);


}
