package com.obtk.mapper;

import com.obtk.domain.Participant;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Repository
public interface ParticipantMapper {

    //通过用户id查询所有参与的活动，关联查询
    @Results(value = {
            //id属性判断是否为主键
            @Result(property = "pId",column = "pId",id=true),
            @Result(property = "status",column = "STATUS"),
            @Result(property = "createTime",column = "createTime"),
            //@One:相当于association   @Many:相当于collection
            //javaType:类实例，不能使用别名的方式  @One(select):子查询的引用。
            @Result(property = "projects",column = "pro_id",
                    javaType = com.obtk.domain.Projects.class,
                    one = @One(select = "com.obtk.mapper.ProjectsMapper.findById")
            )
    })
    @Select("SELECT pId,STATUS,createTime,pro_id FROM participant WHERE userId=#{userId}")
    List<Participant> findAll(@Param("userId") int userId);

    //根据pid删除单个签到活动（数据备份使用）
    @Delete("delete from participant where pId=#{pid}")
    int delOne(int pid);

    //根据proid/userid删除单个签到活动（数据备份使用）
    @Delete("delete from participant where pro_id=#{proid} and userId=#{userid}")
    int delOneBypuid(@Param("proid") int proid,@Param("userid") int userid);


    //根据pid查询单个签到活动（数据备份使用）
    @Select("select * from participant where pId=#{pid}")
    Participant findOne(int pid);

    //根据userid和proid查询单个签到活动（判断是否已经加入该活动）
    @Select("select * from participant where pro_id=#{proid} and userId=#{userId}")
    Participant findByuidAndproid(@Param("userId") int userId,@Param("proid") int proid);

    //添加报名数据
    @Insert("insert into participant values(null,#{userId},#{pro_id},#{pname},#{phone},#{diy3},#{diy4},#{diy5},0,now(),null,null)")
    int addOne(Participant participant);

    //根据参与者编号和状态（未签到）查询所有活动id集合
    @Select("select pro_id from participant where userId=#{userid} and status=0")
    List<Integer> findProidList(@Param("userid") int userid);

    //根据参与者编号和活动编号，更改签到状态
    @Update("update participant set status=1 where userId=#{userid} and pro_id=#{proid}")
    int updStatus(@Param("userid") int userid,@Param("proid") int proid);

    //根据活动id查询所有参与者  （发邮箱用到的报名数据）
    @Select("select pname,phone,diy3,diy4,diy5,status from participant where pro_id=#{proid}")
    List<Participant> findByproid(@Param("proid") int proid);

    //根据活动id查询未签到的参与者/成功签到的参与者（用于发邮箱的签到数据）
    @Select("select pname,phone,diy3,diy4,diy5 from participant where pro_id=#{proid} and status=#{status}")
    List<Participant> findUn(@Param("proid") int proid,@Param("status") int status);

    //获取所有活动参与数
    @Select("SELECT COUNT(pId) FROM participant")
    int getTotalPpt();

    //获取所有活动实际签到数（签到失败也算）
    @Select("SELECT COUNT(pId) FROM participant WHERE STATUS!=0")
    int getTotalSign();
}
