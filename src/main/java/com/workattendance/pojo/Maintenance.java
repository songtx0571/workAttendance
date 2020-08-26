package com.workattendance.pojo;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Maintenance {

    private int id;

    private String datetime;

    private int projectId;

    private String leader;
    private String leaderName;


    private int num;

    private int attendance;

    private double totalTime;


    public Maintenance(int id, String datetime, int projectId, String leader, int num, int attendance) {
        super();
        this.id = id;
        this.datetime = datetime;
        this.projectId = projectId;
        this.leader = leader;
        this.num = num;
        this.attendance = attendance;
    }

    public Maintenance() {
        super();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDatetime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        try {
            datetime = sdf1.format(sdf.parse(datetime));
        } catch (ParseException e) {
            try {
                datetime = sdf.format(sdf1.parse(datetime));
            } catch (ParseException e1) {

            }
        }
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public String getLeader() {
        return leader;
    }

    public void setLeader(String leader) {
        this.leader = leader;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getAttendance() {
        return attendance;
    }

    public void setAttendance(int attendance) {
        this.attendance = attendance;
    }

    public double getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(double totalTime) {
        this.totalTime = totalTime;
    }

    @Override
    public String toString() {
        return "Maintenance [id=" + id + ", datetime=" + datetime + ", projectId=" + projectId + ", leader=" + leader
                + ", leaderName=" + leaderName + ", num=" + num + ", attendance=" + attendance
                + ", totalTime=" + totalTime + "]";
    }

    public String getLeaderName() {
        return leaderName;
    }

    public void setLeaderName(String leaderName) {
        this.leaderName = leaderName;
    }


}
