package com.howei.pojo;

/**
 * 运行工时
 */
public class OperatingHours {

    private Integer id;
    private Integer scrdailyId;//运行交接班
    private Integer employeeId;//员工
    private String monthDay;//操作日期
    private String tradersTime;//交班人时间

    private String successorTime;//接班时间
    private int projectId;//项目部
    private int other;
    private double workingTime;//工时

    //虚字段
    private String employeeName;
    private String employeeNumber;
    private double monthTime;//本月工时
    private double workOvertime;//加班工时
    private int workAttendance;//考勤天数

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getScrdailyId() {
        return scrdailyId;
    }

    public void setScrdailyId(Integer scrdailyId) {
        this.scrdailyId = scrdailyId;
    }

    public String getMonthDay() {
        return monthDay;
    }

    public void setMonthDay(String monthDay) {
        this.monthDay = monthDay;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public String getTradersTime() {
        return tradersTime;
    }

    public void setTradersTime(String tradersTime) {
        this.tradersTime = tradersTime;
    }

    public String getSuccessorTime() {
        return successorTime;
    }

    public void setSuccessorTime(String successorTime) {
        this.successorTime = successorTime;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public int getOther() {
        return other;
    }

    public void setOther(int other) {
        this.other = other;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getEmployeeNumber() {
        return employeeNumber;
    }

    public void setEmployeeNumber(String employeeNumber) {
        this.employeeNumber = employeeNumber;
    }

    public double getWorkingTime() {
        return workingTime;
    }

    public double getMonthTime() {
        return monthTime;
    }

    public void setMonthTime(double monthTime) {
        this.monthTime = monthTime;
    }

    public double getWorkOvertime() {
        return workOvertime;
    }

    public void setWorkOvertime(double workOvertime) {
        this.workOvertime = workOvertime;
    }

    public int getWorkAttendance() {
        return workAttendance;
    }

    public void setWorkAttendance(int workAttendance) {
        this.workAttendance = workAttendance;
    }

    public void setWorkingTime(double workingTime) {
        this.workingTime = workingTime;
    }
}
