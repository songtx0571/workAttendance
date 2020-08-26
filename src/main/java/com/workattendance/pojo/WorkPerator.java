package com.workattendance.pojo;

/**
 * 运行专工
 */
public class WorkPerator {
    private int id;
    private int userId;
    private String created;
    private String updated;
    private int createdBy;
    private int updatedBy;
    private int priority;//优先级：
    private String patrolTask;//巡检任务
    private int artificialNumber;//人工巡检数量
    private int aiNumber;//ai巡检数量
    private String planTime;//计划时间
     private int status;//状态:0为刚创建，1为启用，2为暂停，3为删除
    private String cycle;//周期
    private String equipment;//设备
    private String measuringType;//测点类型
    private String unit;//单位
    private String projectDepartment;//项目部
    private int parent;// 父类
    // 虚字段
    private int edit;
    private String userName;//专工Name
    private String sysName;//系统号
    private String unitName;//单位名称
    private String departmentName;//项目部

    public int getId() {

        return id;
    }

    public void setId(int id) {

        this.id = id;
        this.edit=id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getPatrolTask() {
        return patrolTask;
    }

    public void setPatrolTask(String patrolTask) {
        this.patrolTask = patrolTask;
    }

    public int getArtificialNumber() {
        return artificialNumber;
    }

    public void setArtificialNumber(int artificialNumber) {
        this.artificialNumber = artificialNumber;
    }

    public int getAiNumber() {
        return aiNumber;
    }

    public void setAiNumber(int aiNumber) {
        this.aiNumber = aiNumber;
    }

    public String getPlanTime() {
        return planTime;
    }

    public void setPlanTime(String planTime) {
        this.planTime = planTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCycle() {
        return cycle;
    }

    public void setCycle(String cycle) {
        this.cycle = cycle;
    }

    public String getEquipment() {
        return equipment;
    }

    public void setEquipment(String equipment) {
        this.equipment = equipment;
    }

    public String getMeasuringType() {
        return measuringType;
    }

    public void setMeasuringType(String measuringType) {
        this.measuringType = measuringType;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getSysName() {
        return sysName;
    }

    public void setSysName(String sysName) {
        this.sysName = sysName;
    }

    public String getProjectDepartment() {
        return projectDepartment;
    }

    public void setProjectDepartment(String projectDepartment) {
        this.projectDepartment = projectDepartment;
    }

    public int getParent() {
        return parent;
    }

    public void setParent(int parent) {
        this.parent = parent;
    }

    public int getEdit() {
        return edit;
    }

    public void setEdit(int edit) {
        this.edit = edit;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }
}
