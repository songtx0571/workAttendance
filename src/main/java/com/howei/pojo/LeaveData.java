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
    private int numValue;//请假类型设定的数值
    private String leaveRemark;//请假类型说明
    private int leaveCount;//根据请假类型
    private Double leaveResult;//请假考核分
    private int leaveUnit;//请假类型单位
    private String leaveUnitName;


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

    public String getLeaveName() {
        return LeaveName;
    }

    public void setLeaveName(String leaveName) {
        LeaveName = leaveName;
    }

    public String getCreatedName() {
        return createdName;
    }

    public void setCreatedName(String createdName) {
        this.createdName = createdName;
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

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
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

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public Integer getManager() {
        return manager;
    }

    public void setManager(Integer manager) {
        this.manager = manager;
    }

    public String getReviewTime() {
        return reviewTime;
    }

    public void setReviewTime(String reviewTime) {
        this.reviewTime = reviewTime;
    }

    public String getReviewResult() {
        return reviewResult;
    }

    public void setReviewResult(String reviewResult) {
        this.reviewResult = reviewResult;
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

    public String getBothTime() {
        return bothTime;
    }

    public void setBothTime(String bothTime) {
        this.bothTime = bothTime;
    }

    public void setNumValue(int numValue) {
        this.numValue = numValue;
    }

    public String getLeaveRemark() {
        return leaveRemark;
    }

    public int getNumValue() {
        return numValue;
    }

    public void setLeaveRemark(String leaveRemark) {
        this.leaveRemark = leaveRemark;
    }

    public int getLeaveCount() {
        return leaveCount;
    }

    public void setLeaveCount(int leaveCount) {
        this.leaveCount = leaveCount;
    }

    public Double getLeaveResult() {
        return leaveResult;
    }

    public void setLeaveResult(Double leaveResult) {
        this.leaveResult = leaveResult;
    }

    public int getLeaveUnit() {
        return leaveUnit;
    }

    public void setLeaveUnit(int leaveUnit) {
        this.leaveUnit = leaveUnit;
    }

    public String getLeaveUnitName() {
        return leaveUnitName;
    }

    public void setLeaveUnitName(String leaveUnitName) {
        this.leaveUnitName = leaveUnitName;
    }
}
