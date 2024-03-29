package com.howei.pojo;

public class Performance {
    private int id;
    private String workTasks;
    private String access;
    private String weights;
    private String score;
    private String cycle;
    private String detail;
    private String manager;
    private int employeeId;
    private Boolean isActive;

    @Override
    public String toString() {
        return "Performance{" +
                "id=" + id +
                ", workTasks='" + workTasks + '\'' +
                ", access='" + access + '\'' +
                ", weights='" + weights + '\'' +
                ", score='" + score + '\'' +
                ", cycle='" + cycle + '\'' +
                ", detail='" + detail + '\'' +
                ", manager='" + manager + '\'' +
                ", employeeId=" + employeeId +
                ", isActive=" + isActive +
                '}';
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getScore() {
        return score;
    }

    public Performance setScore(String score) {
        this.score = score;
        return this;
    }

    public String getCycle() {
        return cycle;
    }

    public Performance setCycle(String cycle) {
        this.cycle = cycle;
        return this;
    }


    public int getId() {
        return id;
    }

    public Performance setId(int id) {
        this.id = id;
        return this;
    }

    public String getWorkTasks() {
        return workTasks;
    }

    public Performance setWorkTasks(String workTasks) {
        this.workTasks = workTasks;
        return this;
    }

    public String getAccess() {
        return access;
    }

    public Performance setAccess(String access) {
        this.access = access;
        return this;
    }

    public String getWeights() {
        return weights;
    }

    public Performance setWeights(String weights) {
        this.weights = weights;
        return this;
    }
}
