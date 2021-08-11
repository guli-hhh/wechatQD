package com.obtk.mapper;

import com.obtk.domain.Projects;
import org.apache.ibatis.annotations.Insert;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectsDataMapper {
    //签到活动数据备份
    @Insert("insert into projectsdata values(null,#{pro_id},#{userId},#{projectName},#{startTime}," +
            "#{endTime},#{place_name},#{place_address},#{latitude},#{longitude},#{qrcode}," +
            "#{add},#{inviteId},#{status},#{diy1},#{diy2},#{diy3},#{diy4},#{diy5},#{enroll},#{finish},#{createTime},null,null)")
    int addOne(Projects projects);
}
