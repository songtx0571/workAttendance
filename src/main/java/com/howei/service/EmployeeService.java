package com.howei.service;

import com.howei.pojo.Employee;

import java.util.List;
import java.util.Map;

public interface EmployeeService {
    List<Employee> getEmployeeList(Map map);

    Employee getEmployeeById(String id);

    int updateEmployee(Employee employee);

    List<Employee> searchEmployee(Map map);

    List<Map> getEmployeeNameMap(Map map);

    List<Map<String,String>> getEmployeeNameMapByManager(String empId);

    List<Employee> getEmployeeByManager(int employeeId);

    Map<Integer,String> getEmployeeMap();

    List<Map<String,String>> getEmpMap(Map map);
}
