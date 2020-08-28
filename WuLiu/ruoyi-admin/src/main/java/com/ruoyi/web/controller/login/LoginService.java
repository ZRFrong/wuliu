package com.ruoyi.web.controller.login;


import com.ruoyi.common.annotation.DataSource;
import com.ruoyi.common.enums.DataSourceType;
import com.ruoyi.system.domain.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface LoginService {


    @DataSource(value = DataSourceType.SLAVE)
    @Select("select d.user_id from db_user_token d where token = #{token}")
    Long selectByAddress(String token);

    @DataSource(value = DataSourceType.SLAVE)
    @Select("select  d.user_login username,user_pass password  from db_user d where id = #{i}")
    loginpojo selectUser(Long i);

    @DataSource(value = DataSourceType.SLAVE)
    @Select("select * from db_user")
    List<userSyn> selectUserList();

    @Select("select * from sys_user where login_name=#{user_login}")
    List<SysUser> selectByUserLogin(String user_login);
}
