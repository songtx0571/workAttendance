package com.howei.mapper;

import com.howei.pojo.Employee;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public interface EmployeeMapper {

    List<Employee> getEmployeeList(Map map);

    Employee getEmployeeById(String id);

    int updateEmployee(Employee employee);

    List<Employee> searchEmployee(Map map);

    List<Employee> getEmployeeNameMap(Map map);

    List<Employee> getEmployeeNameMapByManager(@Param("empId") String empId);

    List<Employee> getEmployeeByManager(@Param("empId") int employeeId);

    List<Employee> getEmployeeMap();

    List<Employee> getEmpMap(Map map);
}
