package com.howei.pojo;

/**
 * 运行岗位
 */
public class PostPerator {

    private int id;
    private int postPeratorId;//运行岗位人员id
    private String created;
    private String updated;
    private int createdBy;
    private int updatedBy;
    private String inspectionStaTime;//巡检开始时间
    private String inspectionEndTime;//巡检结束时间
    private String inspectionEndTheoryTime;//理论结束时间
    private int peratorId;//运行专工模板id
    private int department;//部门

    //虚字段
    private String postPeratorName;//运行岗位人员Name
    private String cycle;//周期
    private String projectDepartment;//项目部
    private String patrolTask;//巡检任务

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPostPeratorId() {
        return postPeratorId;
    }

    public void setPostPeratorId(int postPeratorId) {
        this.postPeratorId = postPeratorId;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public int getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }

    public int getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(int updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getInspectionStaTime() {
        return inspectionStaTime;
    }

    public void setInspectionStaTime(String inspectionStaTime) {
        this.inspectionStaTime = inspectionStaTime;
    }

    public String getInspectionEndTime() {
        return inspectionEndTime;
    }

    public void setInspectionEndTime(String inspectionEndTime) {
        this.inspectionEndTime = inspectionEndTime;
    }

    public int getPeratorId() {
        return peratorId;
    }

    public void setPeratorId(int peratorId) {
        this.peratorId = peratorId;
    }

    public String getPostPeratorName() {
        return postPeratorName;
    }

    public void setPostPeratorName(String postPeratorName) {
        this.postPeratorName = postPeratorName;
    }

    public String getInspectionEndTheoryTime() {
        return inspectionEndTheoryTime;
    }

    public void setInspectionEndTheoryTime(String inspectionEndTheoryTime) {
        this.inspectionEndTheoryTime = inspectionEndTheoryTime;
    }

    public int getDepartment() {
        return department;
    }

    public void setDepartment(int department) {
        this.department = department;
    }
}
