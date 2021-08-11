package com.obtk.mapper;

import com.obtk.domain.Projects;
import org.apache.ibatis.annotations.*;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectsMapper {

    //根据用户id查询所有创建签到活动
    @Select("SELECT pro_id,projectName,startTime,endTime,place_name,place_address,qrcode,inviteId,STATUS,enroll," +
            "finish,createTime FROM projects WHERE userId=#{userId}")
    List<Projects> findAll(@Param("userId") int userId);

    //添加签到活动
    @Insert("insert into projects values(null,#{userId},#{projectName},#{startTime}," +
            "#{endTime},#{place_name},#{place_address},#{latitude},#{longitude},#{qrcode}," +
            "#{add},#{inviteId},#{status},#{diy1},#{diy2},#{diy3},#{diy4},#{diy5},#{enroll},#{finish},#{createTime},null,null)")
    int addOne(Projects projects);

    //根据活动id查询单个签到活动(页面展示使用数据)
    @Select("SELECT pro_id,projectName,startTime,endTime,place_name,place_address,qrcode,inviteId,STATUS,enroll," +
            "finish,createTime FROM projects WHERE pro_id=#{pro_id}")
    Projects findById(@Param("pro_id") int proId);

    //根据活动id修改活动状态，页面展示即更新活动状态
    @Update("update projects set status=#{status} where pro_id=#{proId}")
    int updStauts(@Param("proId") int proId, @Param("status") int status);

    //根据活动id查询单个签到活动
    @Select("select * from projects where pro_id=#{pro_id}")
    Projects findOne(@Param("pro_id") int proId);

    //根据活动id删除单个活动签到
    @Delete("DELETE FROM projects WHERE pro_id=#{pro_id}")
    int delOne(@Param("pro_id") int proId);

    //根据活动邀请码查询单个
    @Select("select * from projects where inviteId=#{inId}")
    Projects findByinviteId(@Param("inId") int inId);

    //根据活动id更改报名人数
    @Update("update projects set enroll=enroll+1 where pro_id=#{pro_id}")
    int updEnroll(@Param("pro_id") int proId);

    //根据活动id、状态码（是否进行中）
    @Select("SELECT pro_id,startTime,endTime,place_name,place_address," +
            "latitude,longitude FROM projects WHERE pro_id=#{pro_id} and status=1")
    Projects findByIdAndStatus(@Param("pro_id") int proId);

    //根据活动id更改完成签到的人数
    @Update("update projects set finish=finish+1 where pro_id=#{pro_id}")
    int updFinish(@Param("pro_id") int proId);

    //查询所有
    @Select("select * from projects ORDER BY createTime DESC")
    List<Projects> findByPage();

    //根据用户昵称模糊查询
    @Select("SELECT * FROM projects WHERE projectName LIKE '%${projectName}%'")
    List<Projects> findLike(@Param("projectName") String projectName);

    //定时任务查询所有
    @Select("select pro_id,startTime,endTime,status from projects where startTime BETWEEN #{zero} AND #{torzero} and STATUS !=2 ")
    List<Projects> findAllTimer(@Param("zero") long zero,@Param("torzero") long torzero);

    //获取活动中最小创建时间
    @Select("SELECT MIN(createTime) FROM projects")
    long getMinCreateTime();

    //获取所有活动数
    @Select("SELECT COUNT(pro_id) FROM projects")
    int getTotalPro();
}
