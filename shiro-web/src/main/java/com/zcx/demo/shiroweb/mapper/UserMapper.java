package com.zcx.demo.shiroweb.mapper;

import com.zcx.demo.shiroweb.vo.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface UserMapper {

    @Select("select username ,password from sys_user where username = #{name}")
    @ResultType(User.class)
    List<User> selectUserByName(@Param("name") String name);

    @Select("select (select sr.role_name from sys_role sr where sr.role_id = sur.role_id) roleName " +
            " from sys_user_role sur join sys_user su on su.user_id = sur.user_id" +
            " where su.username = #{name}")
    List<String> selectRoleByName(String name);


    List<String> selectPermissionByRoles(List<String> role);
}
