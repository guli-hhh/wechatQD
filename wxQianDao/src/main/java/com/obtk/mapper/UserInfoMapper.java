package com.obtk.mapper;

import com.obtk.domain.UserInfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserInfoMapper {
    //根据id查询单个
    @Select("select * from userInfo where userId=#{userid}")
    UserInfo findOne(@Param("userid") int userid);

    //添加单个后台用户
    @Insert("insert into userInfo values(#{userId},#{nickName},#{avatarUrl},#{city},#{country},#{gender},#{language},#{province},now(),now(),1,null,null)")
    int addOne(UserInfo userInfo);

    //查询所有
    @Select("select * from userinfo ORDER BY recentlyTime DESC")
    List<UserInfo> findAll();

    //根据用户昵称模糊查询
    @Select("SELECT * FROM userinfo WHERE nickName LIKE '%${nickName}%'")
    List<UserInfo> findLike(@Param("nickName") String nickName);

    //根据id去改最近登录时间和登录次数
    @Update("update userinfo set recentlyTime=now(),loginNum=loginNum+1 where userId=#{userid}")
    int updUser(@Param("userid") int userid);

    //统计本周或者本月新增用户
    @Select("SELECT count(*) FROM userinfo WHERE warrantTime BETWEEN #{start} AND #{end} ")
    int getOWuser(@Param("start") String start,@Param("end") String end);

    //禁用或者启用用户
    @Update("update userinfo set spare1=#{str} where userId=#{userid}")
    int forbidden(@Param("userid") int userid,@Param("str") String str);

    //标星或取消用户
    @Update("update userinfo set spare2=#{mark} where userId=#{userid}")
    int mark(@Param("userid") int userid,@Param("mark") String mark);
}
