package com.workattendance.service.impl;

import com.workattendance.mapper.UsersMapper;
import com.workattendance.pojo.Users;
import com.workattendance.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
