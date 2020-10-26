package com.howei.mapper;

import com.howei.pojo.Users;
import com.howei.pojo.Achievement;
import com.howei.pojo.Behavior;
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

    List<Achievement> findAchievementsByUserName1(@Param("behavior") Behavior behavior);

    Users loginUserNumber(String userNumber);

    Users getUserRolesByName(@Param("userNumber") String userNumber);

    Users getUserByEmpId(@Param("employeeId") Integer employeeId);
}
