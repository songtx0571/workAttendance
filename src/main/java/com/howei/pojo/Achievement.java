package com.howei.pojo;

public class Achievement {

    private int id;

    private String userName;

    private String name;

    private int week;

    private int result1;

    private int result2;

    private int result3;

    private int result4;

    private String cycle;

    private int times;

    @Override
    public String toString() {
        return "Achievement{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", name='" + name + '\'' +
                ", week=" + week +
                ", result1=" + result1 +
                ", result2=" + result2 +
                ", result3=" + result3 +
                ", result4=" + result4 +
                ", cycle='" + cycle + '\'' +
                ", times=" + times +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Achievement() {
        super();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    public String getResult1() {
        if (result1 == -1) {
            return "";
        } else {
            return result1 + "";
        }
    }

    public void setResult1(int result1) {
        this.result1 = result1;
    }

    public String getResult2() {
        if (result2 == -1) {
            return "";
        } else {
            return result2 + "";
        }
    }

    public void setResult2(int result2) {
        this.result2 = result2;
    }

    public String getResult3() {
        if (result3 == -1) {
            return "";
        } else {
            return result3 + "";
        }
    }

    public void setResult3(int result3) {
        this.result3 = result3;
    }

    public String getResult4() {
        if (result4 == -1) {
            return "";
        } else {
            return result4 + "";
        }
    }

    public void setResult4(int result4) {
        this.result4 = result4;
    }

    public String getCycle() {
        return cycle;
    }

    public void setCycle(String cycle) {
        this.cycle = cycle;
    }

    public int getTimes() {
        return times;
    }

    public void setTimes(int times) {
        this.times = times;
    }


    public int getMaxValue() {
        int maxValue = 0;
        maxValue = (result1 > maxValue) ? result1 : maxValue;
        maxValue = (result2 > maxValue) ? result2 : maxValue;
        maxValue = (result3 > maxValue) ? result3 : maxValue;
        maxValue = (result4 > maxValue) ? result4 : maxValue;
        return maxValue;
    }
}
