package com.workattendance.pojo;

public class PostPeratorData {
    private int id;
    private String measuringType;//测点类型
    private String measuringTypeData;//数据
    private String equipment;//设备名称
    private String created;
    private int createdBy;
    private int postPeratorId;//员工执行模板id
    private String unit;//单位
    private int ind;//执行标识

    //虚字段
    private String equipmentName;//设备名称
    private String patrolTask;//任务名称
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMeasuringType() {
        return measuringType;
    }

    public void setMeasuringType(String measuringType) {
        this.measuringType = measuringType;
    }

    public String getMeasuringTypeData() {
        return measuringTypeData;
    }

    public void setMeasuringTypeData(String measuringTypeData) {
        this.measuringTypeData = measuringTypeData;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public int getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }

    public int getPostPeratorId() {
        return postPeratorId;
    }

    public void setPostPeratorId(int postPeratorId) {
        this.postPeratorId = postPeratorId;
    }

    public String getEquipmentName() {
        return equipmentName;
    }

    public void setEquipmentName(String equipmentName) {
        this.equipmentName = equipmentName;
    }

    public String getPatrolTask() {
        return patrolTask;
    }

    public void setPatrolTask(String patrolTask) {
        this.patrolTask = patrolTask;
    }

    public int getInd() {
        return ind;
    }

    public void setInd(int ind) {
        this.ind = ind;
    }

    public String getEquipment() {
        return equipment;
    }

    public void setEquipment(String equipment) {
        this.equipment = equipment;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
