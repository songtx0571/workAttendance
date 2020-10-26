package com.howei.service;

import com.howei.pojo.Authority;
import com.howei.pojo.Role;
import com.howei.util.LayuiTree;

import java.util.List;
import java.util.Map;

public interface RoleService {
     List<Role> selectRole();

    Role getRoleById(String id);

    List<Map<String,String>> getRolesMap();

    List<Role> getRoleAuthorityList(Map map);

    List<Map<String,String>> getAuthorityMap(Map map);

    List<Authority> getRoleAuthoritys(String roleId);

    List<LayuiTree> getURLList(String parentId);

    List<Authority> getAuthIdByRoleId(String roleId);
}
