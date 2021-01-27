package com.howei.service;

import com.howei.pojo.Users;

import java.util.List;
import java.util.Map;

public interface UserService {

    /**
     * 登陆验证
     * @param userName
     * @param password
     * @return
     */
    public Users findUser(String userName, String password);

    /**
     * 根据roleId查询当前用户的权限
     * @param roleId
     * @return
     */
    public String getPermission(int roleId);

    Users findById(String id);

    List<Users> getProjectIdS(String userName);

    List<Users> getNameByProjectId(Integer projectId);

    Users findByUserName(String userName);

    Users loginUserNumber(String userNumber);

    Users getUserRolesByName(String userNumber);

    Users getUserByEmpId(Integer employeeId);

    List<Map> getNameByEmployeeIds(String empIdStr);
}
