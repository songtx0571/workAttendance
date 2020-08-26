package com.workattendance.service;

import com.workattendance.pojo.Users;

import java.util.List;

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
}
