package com.howei.service.impl;

import com.howei.mapper.UsersMapper;
import com.howei.pojo.Users;
import com.howei.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UsersMapper usersMapper;

    @Override
    public Users findUser(String userName, String password) {
        return usersMapper.findUser(userName,password);
    }

    @Override
    public String getPermission(int roleId) {
        return usersMapper.getPermission(roleId);
    }

    @Override
    public Users findById(String id) {
        return usersMapper.findById(id);
    }

    @Override
    public List<Users> getProjectIdS(String userName) {
        return usersMapper.getProjectIdS(userName);
    }

    @Override
    public List<Users> getNameByProjectId(Integer projectId) {
        return usersMapper.getNameByProjectId(projectId);
    }

    @Override
    public Users findByUserName(String userName) {
        return usersMapper.findByUserName(userName);
    }

    @Override
    public Users loginUserNumber(String userNumber) {
        return usersMapper.loginUserNumber(userNumber);
    }

    @Override
    public Users getUserRolesByName(String userNumber) {
        return usersMapper.getUserRolesByName(userNumber);
    }

    @Override
    public Users getUserByEmpId(Integer employeeId) {
        return usersMapper.getUserByEmpId(employeeId);
    }

    @Override
    public List<Map> getNameByEmployeeIds(String empIdStr) {
        return  usersMapper.getByEmployeeIds(empIdStr);
    }
}
