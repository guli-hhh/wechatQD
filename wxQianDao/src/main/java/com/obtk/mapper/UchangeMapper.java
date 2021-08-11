package com.obtk.mapper;

import com.obtk.domain.Uchange;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface UchangeMapper {
    //查询openid对应的小程序用户是否在存在后端
    @Select("select count(*) num from uchange where openId=#{openid}")
    int findUserId(@Param("openid") String openid);

    @Select("select userId from uchange where openId=#{openid}")
    int findUserId1(@Param("openid") String openid);

    //没添加过的openid 则添加到关联表中
    @Insert("insert into uchange values(null,#{openId},#{session_key},null,null)")
    int addOne(Uchange uchange);
}
