package com.howei.pojo;

/**
 * 审核
 */
public class Review {

    private Integer id;
    private Integer leaveDataId;
    private Integer reviewer;//审核人
    private Integer leaveEmployeeId;//请假人
    private Integer result;//审核结果

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getLeaveDataId() {
        return leaveDataId;
    }

    public void setLeaveDataId(Integer leaveDataId) {
        this.leaveDataId = leaveDataId;
    }

    public Integer getReviewer() {
        return reviewer;
    }

    public void setReviewer(Integer reviewer) {
        this.reviewer = reviewer;
    }

    public Integer getLeaveEmployeeId() {
        return leaveEmployeeId;
    }

    public void setLeaveEmployeeId(Integer leaveEmployeeId) {
        this.leaveEmployeeId = leaveEmployeeId;
    }

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }
}
