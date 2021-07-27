package com.howei.pojo;

public class ManagerHours {
    private Integer id;
    private Integer departmentId;
    private Integer employeeId;
    private String monthDay;
    private String workStartTime;
    private String workEndTime;
    private String workIngHour;
    private Integer type;

    @Override
    public String toString() {
        return "ManagerHours{" +
                "id=" + id +
                ", departmentId=" + departmentId +
                ", employeeId=" + employeeId +
                ", monthDay='" + monthDay + '\'' +
                ", workStartTime='" + workStartTime + '\'' +
                ", workEndTime='" + workEndTime + '\'' +
                ", workIngHour='" + workIngHour + '\'' +
                ", type=" + type +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public String getMonthDay() {
        return monthDay;
    }

    public void setMonthDay(String monthDay) {
        this.monthDay = monthDay;
    }

    public String getWorkStartTime() {
        return workStartTime;
    }

    public void setWorkStartTime(String workStartTime) {
        this.workStartTime = workStartTime;
    }

    public String getWorkEndTime() {
        return workEndTime;
    }

    public void setWorkEndTime(String workEndTime) {
        this.workEndTime = workEndTime;
    }

    public String getWorkIngHour() {
        return workIngHour;
    }

    public void setWorkIngHour(String workIngHour) {
        this.workIngHour = workIngHour;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
