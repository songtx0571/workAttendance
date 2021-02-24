package com.howei.service.impl;

import com.howei.pojo.Employee;
import com.howei.mapper.EmployeeMapper;
import com.howei.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    @Override
    public List<Employee> getEmployeeList(Map map) {
        return employeeMapper.getEmployeeList(map);
    }

    @Override
    public Employee getEmployeeById(String id) {
        return employeeMapper.getEmployeeById(id);
    }

    @Override
    public int updateEmployee(Employee employee) {
        return employeeMapper.updateEmployee(employee);
    }

    @Override
    public List<Employee> searchEmployee(Map map) {
        return employeeMapper.searchEmployee(map);
    }

    @Override
    public List<Map> getEmployeeNameMap(Map map1) {
        List<Employee> list=employeeMapper.getEmployeeNameMap(map1);
        List<Map> result=new ArrayList<>();
        if(list!=null){
            for (Employee employee:list){
                Map map=new HashMap();
                map.put("id",employee.getId());
                map.put("name",employee.getName());
                result.add(map);
            }
        }
        return result;
    }

    @Override
    public List<Map<String, String>> getEmployeeNameMapByManager(String empId) {
        List<Employee> list=employeeMapper.getEmployeeNameMapByManager(empId);
        List<Map<String,String>> result=new ArrayList<>();
        if(list!=null){
            for (Employee emp:list) {
                Map<String,String> map=new HashMap<>();
                map.put("id",emp.getId()+"");
                map.put("name",emp.getName());
                result.add(map);
            }
        }
        return result;
    }

    @Override
    public List<Employee> getEmployeeByManager(int employeeId) {
        return employeeMapper.getEmployeeByManager(employeeId);
    }

    @Override
    public Map<Integer, String> getEmployeeMap() {
        List<Employee> list=employeeMapper.getEmployeeMap();
        Map<Integer,String> result=new HashMap<>();
        if(list!=null&&list.size()>0){
            for(Employee employee:list){
                result.put(employee.getId(), employee.getName());
            }
        }
        return result;
    }

    @Override
    public List<Map<String, String>> getEmpMap(Map map) {
        List<Employee> list=employeeMapper.getEmpMap(map);
        List<Map<String, String>> result=new ArrayList<>();
        if(list!=null&&list.size()>0){
            for(Employee employee:list){
                Map<String,String> map1=new HashMap<>();
                map1.put("id",employee.getId()+"");
                map1.put("name", employee.getName());
                result.add(map1);
            }
        }
        return result;
    }
}
