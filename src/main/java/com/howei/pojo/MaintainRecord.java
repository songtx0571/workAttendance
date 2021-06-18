package com.howei.pojo;


import java.util.Date;

public class MaintainRecord {

    private Integer id;
    private Integer maintainId;
    private String maintainRecordNo;
    private Integer departmentId;
    private String employeeId;
    private String workFeedback;
    private String claimTime;
    private String startTime;
    private String endTime;
    private String workingHour;
    private String status;
    private Date createTime;
    private Date updateTime;

    private String systemName;
    private String equipmentName;
    private String unitName;
    private String workContent;
    private String employeeName;

    @Override
    public String toString() {
        return "MaintainRecord{" +
                "id=" + id +
                ", maintainId=" + maintainId +
                ", maintainRecordNo='" + maintainRecordNo + '\'' +
                ", departmentId=" + departmentId +
                ", employeeId='" + employeeId + '\'' +
                ", workFeedback='" + workFeedback + '\'' +
                ", claimTime='" + claimTime + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", workingHour='" + workingHour + '\'' +
                ", status='" + status + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", systemName='" + systemName + '\'' +
                ", equipmentName='" + equipmentName + '\'' +
                ", unitName='" + unitName + '\'' +
                ", workContent='" + workContent + '\'' +
                ", employeeName='" + employeeName + '\'' +
                '}';
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getWorkContent() {
        return workContent;
    }

    public void setWorkContent(String workContent) {
        this.workContent = workContent;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public String getEquipmentName() {
        return equipmentName;
    }

    public void setEquipmentName(String equipmentName) {
        this.equipmentName = equipmentName;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMaintainId() {
        return maintainId;
    }

    public void setMaintainId(Integer maintainId) {
        this.maintainId = maintainId;
    }

    public String getMaintainRecordNo() {
        return maintainRecordNo;
    }

    public void setMaintainRecordNo(String maintainRecordNo) {
        this.maintainRecordNo = maintainRecordNo;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getWorkFeedback() {
        return workFeedback;
    }

    public void setWorkFeedback(String workFeedback) {
        this.workFeedback = workFeedback;
    }

    public String getClaimTime() {
        return claimTime;
    }

    public void setClaimTime(String claimTime) {
        this.claimTime = claimTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getWorkingHour() {
        return workingHour;
    }

    public void setWorkingHour(String workingHour) {
        this.workingHour = workingHour;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
