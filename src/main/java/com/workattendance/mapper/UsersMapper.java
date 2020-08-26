package com.workattendance.mapper;

import com.workattendance.pojo.Users;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface UsersMapper {

    /**
     * 登陆验证
     * @param userName
     * @param password
     * @return
     */
    Users findUser(@Param("UserName") String userName, @Param("Password") String password);

    /**
     * 查询权限
     * @param roleId
     * @return
     */
    public String getPermission(int roleId);

    /**
     * 根据id查询
     * @param id
     * @return
     */
    Users findById(@Param("id") String id);

    List<Users> getProjectIdS(String userName);

    List<Users> getNameByProjectId(@Param("projectId") Integer projectId);

    Users findByUserName(@Param("userName") String userName);
}
