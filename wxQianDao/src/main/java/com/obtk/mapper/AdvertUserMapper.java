package com.obtk.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdvertUserMapper {
    //添加用户点击广告记录
    @Insert("insert into advertuser values(null,#{userId},#{adid},now(),null,null)")
    int addAdUser(@Param("userId") int userId,@Param("adid") int adid);

    //根据广告id查询所有点击广告的用户。
    @Select("SELECT DISTINCT(userId) userId FROM advertuser where ad_id=#{adid} order by userId")
    List<Integer> getUserInfo(@Param("adid") int adid);

    //根据id统计一天每个时间段的点击次数
    @Select("SELECT COUNT(*) FROM advertuser WHERE clickTime BETWEEN #{dstart} AND #{dend} AND ad_id=#{adid} AND clickTime BETWEEN #{start} AND #{end}")
    int getClickNum(@Param("adid") int adid,@Param("dstart") String dstart,@Param("dend") String dend,@Param("start") String start,@Param("end") String end);
}
