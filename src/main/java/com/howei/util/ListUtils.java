package com.howei.util;

import com.howei.pojo.Employee;

import java.util.List;

public class ListUtils {

    /**
     * @param rootList        叶子节点
     * @param allEmployeeList 所有员工
     * @param employeeIdList  查询到的用户的Id
     * @param userNumberList  查询到的用户的编号
     */
    public static void getChildEmployeeId(List<Employee> rootList, List<Employee> allEmployeeList, List<String> employeeIdList, List<String> userNumberList) {
        //没有叶子节点则返回
        if (rootList == null || rootList.size() == 0) {
            return;
        }
        //循环叶子节点,在List集合中添加循环的id
        for (Employee employee : rootList) {
            if (!employeeIdList.contains(employee.getId().toString())) {
                if (employeeIdList != null) {
                    employeeIdList.add(employee.getId().toString());
                }
                if (userNumberList != null) {
                    userNumberList.add(employee.getUserNumber());
                }
            }
        }
        rootList.clear();
        //循环所有员工,如果其管理人id在节点上,则将该用户添加到rootList中,并添加到员工id集合里面
        if (allEmployeeList != null && allEmployeeList.size() > 0) {
            for (Employee employee : allEmployeeList) {
                if (employeeIdList.contains(employee.getManager().toString())) {
                    if (!employeeIdList.contains(employee.getId().toString())) {
                        rootList.add(employee);
                        if (employeeIdList != null) {
                            employeeIdList.add(employee.getId().toString());
                        }
                        if (userNumberList != null) {
                            userNumberList.add(employee.getUserNumber());
                        }
                    }
                }

            }
        }
        //递归查找
        getChildEmployeeId(rootList, allEmployeeList, employeeIdList, userNumberList);

    }

}
