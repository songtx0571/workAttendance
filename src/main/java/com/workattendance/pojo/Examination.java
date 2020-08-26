package com.workattendance.pojo;


public class Examination {
    private int id;
    private String UserName;
    private String Cycle;
    private Integer Week;
    private Integer Times;
    private Integer QuestionId;
    private String Name;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getCycle() {
        return Cycle;
    }

    public void setCycle(String cycle) {
        Cycle = cycle;
    }

    public Integer getWeek() {
        return Week;
    }

    public void setWeek(Integer week) {
        Week = week;
    }

    public Integer getTimes() {
        return Times;
    }

    public void setTimes(Integer times) {
        Times = times;
    }

    public Integer getQuestionId() {
        return QuestionId;
    }

    public void setQuestionId(Integer questionId) {
        QuestionId = questionId;
    }

    @Override
    public String toString() {
        return "Examination{" +
                "id=" + id +
                ", UserName='" + UserName + '\'' +
                ", Cycle='" + Cycle + '\'' +
                ", Week=" + Week +
                ", Times=" + Times +
                ", QuestionId=" + QuestionId +
                ", Name='" + Name + '\'' +
                '}';
    }
}
