package com.obtk.mapper;

import com.obtk.domain.Participant;
import org.apache.ibatis.annotations.Insert;
import org.springframework.stereotype.Repository;

@Repository
public interface ParticipantDataMapper {

    //添加参与者签到活动备份数据
    @Insert("insert into participantdata values(null,#{pId},#{userId},#{pro_id},#{pname},#{phone},#{diy3},#{diy4},#{diy5},#{status},#{createTime},null,null)")
    int addOne(Participant participant);
}
