package com.howei.pojo;

public class LeaveData {

    private Integer id;
    private Integer leaveId;
    private Integer employeeId;
    private String startTime;
    private String endTime;
    private String intervalTime;
    private String created;
    private Integer createdBy;
    private String updated;
    private Integer updatedBy;
    private Integer status;
    private Integer parent;
    private String remark;
    private Integer departmentId;
    private String review;//审核0为同意，1为驳回
    private String reviewRemark;//审核意见
    private String reviewTime;//审核时间
    private Integer nextManager;//下一个审核人
    private Integer excess;//超额标识:0为未超额，1为超额

    //虚字段
    private Integer manager;//直接绩效管理人
    private String employeeName;
    private String createdName;
    private String LeaveName;
    private String total;
    private String statusName;
    private String reviewResult;
    private String bothTime;//两个日期之间间隔
    private Integer numValue;//请假类型设定的数值
    private String leaveRemark;//请假类型说明
    private Integer leaveCount;//根据请假类型
    private Double leaveResult;//请假考核分
    private Integer leaveUnit;//请假类型单位
    private String leaveUnitName;

    @Override
    public String toString() {
        return "LeaveData{" +
                "id=" + id +
                ", leaveId=" + leaveId +
                ", employeeId=" + employeeId +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", intervalTime='" + intervalTime + '\'' +
                ", created='" + created + '\'' +
                ", createdBy=" + createdBy +
                ", updated='" + updated + '\'' +
                ", updatedBy=" + updatedBy +
                ", status=" + status +
                ", parent=" + parent +
                ", remark='" + remark + '\'' +
                ", departmentId=" + departmentId +
                ", review='" + review + '\'' +
                ", reviewRemark='" + reviewRemark + '\'' +
                ", reviewTime='" + reviewTime + '\'' +
                ", nextManager=" + nextManager +
                ", excess=" + excess +
                ", manager=" + manager +
                ", employeeName='" + employeeName + '\'' +
                ", createdName='" + createdName + '\'' +
                ", LeaveName='" + LeaveName + '\'' +
                ", total='" + total + '\'' +
                ", statusName='" + statusName + '\'' +
                ", reviewResult='" + reviewResult + '\'' +
                ", bothTime='" + bothTime + '\'' +
                ", numValue=" + numValue +
                ", leaveRemark='" + leaveRemark + '\'' +
                ", leaveCount=" + leaveCount +
                ", leaveResult=" + leaveResult +
                ", leaveUnit=" + leaveUnit +
                ", leaveUnitName='" + leaveUnitName + '\'' +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getLeaveId() {
        return leaveId;
    }

    public void setLeaveId(Integer leaveId) {
        this.leaveId = leaveId;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
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

    public String getIntervalTime() {
        return intervalTime;
    }

    public void setIntervalTime(String intervalTime) {
        this.intervalTime = intervalTime;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public Integer getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Integer updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getParent() {
        return parent;
    }

    public void setParent(Integer parent) {
        this.parent = parent;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getReviewRemark() {
        return reviewRemark;
    }

    public void setReviewRemark(String reviewRemark) {
        this.reviewRemark = reviewRemark;
    }

    public String getReviewTime() {
        return reviewTime;
    }

    public void setReviewTime(String reviewTime) {
        this.reviewTime = reviewTime;
    }

    public Integer getNextManager() {
        return nextManager;
    }

    public void setNextManager(Integer nextManager) {
        this.nextManager = nextManager;
    }

    public Integer getExcess() {
        return excess;
    }

    public void setExcess(Integer excess) {
        this.excess = excess;
    }

    public Integer getManager() {
        return manager;
    }

    public void setManager(Integer manager) {
        this.manager = manager;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getCreatedName() {
        return createdName;
    }

    public void setCreatedName(String createdName) {
        this.createdName = createdName;
    }

    public String getLeaveName() {
        return LeaveName;
    }

    public void setLeaveName(String leaveName) {
        LeaveName = leaveName;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getReviewResult() {
        return reviewResult;
    }

    public void setReviewResult(String reviewResult) {
        this.reviewResult = reviewResult;
    }

    public String getBothTime() {
        return bothTime;
    }

    public void setBothTime(String bothTime) {
        this.bothTime = bothTime;
    }

    public Integer getNumValue() {
        return numValue;
    }

    public void setNumValue(Integer numValue) {
        this.numValue = numValue;
    }

    public String getLeaveRemark() {
        return leaveRemark;
    }

    public void setLeaveRemark(String leaveRemark) {
        this.leaveRemark = leaveRemark;
    }

    public Integer getLeaveCount() {
        return leaveCount;
    }

    public void setLeaveCount(Integer leaveCount) {
        this.leaveCount = leaveCount;
    }

    public Double getLeaveResult() {
        return leaveResult;
    }

    public void setLeaveResult(Double leaveResult) {
        this.leaveResult = leaveResult;
    }

    public Integer getLeaveUnit() {
        return leaveUnit;
    }

    public void setLeaveUnit(Integer leaveUnit) {
        this.leaveUnit = leaveUnit;
    }

    public String getLeaveUnitName() {
        return leaveUnitName;
    }

    public void setLeaveUnitName(String leaveUnitName) {
        this.leaveUnitName = leaveUnitName;
    }
}
