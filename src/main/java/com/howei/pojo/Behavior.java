package com.howei.pojo;

import java.util.List;

public class Behavior {
    private Integer id;
    private String week1;
    private String week2;
    private String week3;
    private String week4;
    private String period;
    private String tiaoxiu;
    private String qingjia;
    private String kuanggong;
    private String chidao;
    private String lunxiu;
    private String chuchai;
    private String remark;
    private String cycle;
    private String sum;
    private Integer employeeId;
    private Double jiaban;
    private Double kaoqin;
    private Double zhiban;

    private List<LeaveData> leaveData;

    @Override
    public String toString() {
        return "Behavior{" +
                "id=" + id +
                ", week1='" + week1 + '\'' +
                ", week2='" + week2 + '\'' +
                ", week3='" + week3 + '\'' +
                ", week4='" + week4 + '\'' +
                ", period='" + period + '\'' +
                ", tiaoxiu='" + tiaoxiu + '\'' +
                ", qingjia='" + qingjia + '\'' +
                ", kuanggong='" + kuanggong + '\'' +
                ", chidao='" + chidao + '\'' +
                ", lunxiu='" + lunxiu + '\'' +
                ", chuchai='" + chuchai + '\'' +
                ", remark='" + remark + '\'' +
                ", cycle='" + cycle + '\'' +
                ", sum='" + sum + '\'' +
                ", employeeId=" + employeeId +
                ", jiaban=" + jiaban +
                ", kaoqin=" + kaoqin +
                ", zhiban=" + zhiban +
                ", leaveData=" + leaveData +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getWeek1() {
        return week1;
    }

    public void setWeek1(String week1) {
        this.week1 = week1;
    }

    public String getWeek2() {
        return week2;
    }

    public void setWeek2(String week2) {
        this.week2 = week2;
    }

    public String getWeek3() {
        return week3;
    }

    public void setWeek3(String week3) {
        this.week3 = week3;
    }

    public String getWeek4() {
        return week4;
    }

    public void setWeek4(String week4) {
        this.week4 = week4;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getTiaoxiu() {
        return tiaoxiu;
    }

    public void setTiaoxiu(String tiaoxiu) {
        this.tiaoxiu = tiaoxiu;
    }

    public String getQingjia() {
        return qingjia;
    }

    public void setQingjia(String qingjia) {
        this.qingjia = qingjia;
    }

    public String getKuanggong() {
        return kuanggong;
    }

    public void setKuanggong(String kuanggong) {
        this.kuanggong = kuanggong;
    }

    public String getChidao() {
        return chidao;
    }

    public void setChidao(String chidao) {
        this.chidao = chidao;
    }

    public String getLunxiu() {
        return lunxiu;
    }

    public void setLunxiu(String lunxiu) {
        this.lunxiu = lunxiu;
    }

    public String getChuchai() {
        return chuchai;
    }

    public void setChuchai(String chuchai) {
        this.chuchai = chuchai;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCycle() {
        return cycle;
    }

    public void setCycle(String cycle) {
        this.cycle = cycle;
    }

    public String getSum() {
        return sum;
    }

    public void setSum(String sum) {
        this.sum = sum;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public Double getJiaban() {
        return jiaban;
    }

    public void setJiaban(Double jiaban) {
        this.jiaban = jiaban;
    }

    public Double getKaoqin() {
        return kaoqin;
    }

    public void setKaoqin(Double kaoqin) {
        this.kaoqin = kaoqin;
    }

    public Double getZhiban() {
        return zhiban;
    }

    public void setZhiban(Double zhiban) {
        this.zhiban = zhiban;
    }

    public List<LeaveData> getLeaveData() {
        return leaveData;
    }

    public void setLeaveData(List<LeaveData> leaveData) {
        this.leaveData = leaveData;
    }
}
