package com.howei.pojo;

import java.math.BigDecimal;

public class Salary {

    private int id;
    private int employeeId;
    private String userName;
    private String name;
    private int jiaban;
    private int zhiban;
    //考勤天数
    private int kaoqin;
    private int score1;
    private int score2;
    private double yanglao;
    private double shiye;
    private double yiliao;
    //当月应纳税所得额
    private double tax1;
    //累计应纳税所得额
    private double taxTotal;
    //累计个税
    private double taxTotal2;
    //当月个税
    private double tax2;
    private String cycle;
    private String datetime;
    private double gongjijin;
    private double zhuanxiang;
    private double qitakouchu;
    private double canbuxishu;
    private double jiabanxishu;
    private double salaryJiaban;
    //净绩效
    private double netPerformance;
    //综合绩效
    private double comprehensivePerformance;
    //基本工资
    private int basicwages;
    //绩效基数
    private double meritpay;
    //餐补
    private double mealSupplement;
    //通讯补助
    private int phoneAllowance;
    //应付工资
    private double wagePayable;
    private String department;
    private String laowupaiqian;
    private int year;
    private int month;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Salary{" +
                "id=" + id +
                ", employeeId=" + employeeId +
                ", userName='" + userName + '\'' +
                ", name='" + name + '\'' +
                ", jiaban=" + jiaban +
                ", zhiban=" + zhiban +
                ", kaoqin=" + kaoqin +
                ", score1=" + score1 +
                ", score2=" + score2 +
                ", yanglao=" + yanglao +
                ", shiye=" + shiye +
                ", yiliao=" + yiliao +
                ", tax1=" + tax1 +
                ", taxTotal=" + taxTotal +
                ", taxTotal2=" + taxTotal2 +
                ", tax2=" + tax2 +
                ", cycle='" + cycle + '\'' +
                ", datetime='" + datetime + '\'' +
                ", gongjijin=" + gongjijin +
                ", zhuanxiang=" + zhuanxiang +
                ", qitakouchu=" + qitakouchu +
                ", canbuxishu=" + canbuxishu +
                ", jiabanxishu=" + jiabanxishu +
                ", salaryJiaban=" + salaryJiaban +
                ", netPerformance=" + netPerformance +
                ", comprehensivePerformance=" + comprehensivePerformance +
                ", basicwages=" + basicwages +
                ", meritpay=" + meritpay +
                ", mealSupplement=" + mealSupplement +
                ", phoneAllowance=" + phoneAllowance +
                ", wagePayable=" + wagePayable +
                ", department='" + department + '\'' +
                ", laowupaiqian='" + laowupaiqian + '\'' +
                ", year=" + year +
                ", month=" + month +
                '}';
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getCycle() {
        return cycle;
    }

    public void setCycle(String cycle) {
        this.cycle = cycle;
    }

    public double getTax2() {
        return tax2;
    }

    public void setTax2(double tax2) {
        this.tax2 = tax2;
    }

    public double getGongjijin() {
        return gongjijin;
    }

    public void setGongjijin(double gongjijin) {
        this.gongjijin = gongjijin;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public double getYanglao() {
        return yanglao;
    }

    public void setYanglao(double yanglao) {
        this.yanglao = yanglao;
    }

    public double getShiye() {
        return shiye;
    }

    public void setShiye(double shiye) {
        this.shiye = shiye;
    }

    public double getYiliao() {
        return yiliao;
    }

    public void setYiliao(double yiliao) {
        this.yiliao = yiliao;
    }


    public double getZhuanxiang() {
        return zhuanxiang;
    }

    public void setZhuanxiang(double zhuanxiang) {
        this.zhuanxiang = zhuanxiang;
    }

    public double getQitakouchu() {
        return qitakouchu;
    }

    public void setQitakouchu(double qitakouchu) {
        this.qitakouchu = qitakouchu;
    }

    public double getCanbuxishu() {
        return canbuxishu;
    }

    public void setCanbuxishu(double canbuxishu) {
        this.canbuxishu = canbuxishu;
    }

    public double getJiabanxishu() {
        return jiabanxishu;
    }

    public void setJiabanxishu(double jiabanxishu) {
        this.jiabanxishu = jiabanxishu;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getJiaban() {
        return jiaban;
    }

    public void setJiaban(int jiaban) {
        this.jiaban = jiaban;
    }

    public int getZhiban() {
        return zhiban;
    }

    public void setZhiban(int zhiban) {
        this.zhiban = zhiban;
    }

    public int getKaoqin() {
        return kaoqin;
    }

    public void setKaoqin(int kaoqin) {
        this.kaoqin = kaoqin;
    }

    public int getScore1() {
        return score1;
    }

    public void setScore1(int score1) {
        this.score1 = score1;
    }

    public int getScore2() {
        return score2;
    }

    public void setScore2(int score2) {
        this.score2 = score2;
    }

    public double getNetPerformance() {
        netPerformance = (score1 * 0.5 + score2 * 0.5) / 90;
        BigDecimal bd = new BigDecimal(netPerformance);
        return bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public void setNetPerformance(double netPerformance) {

        this.netPerformance = netPerformance;
    }

    public double getComprehensivePerformance() {
        comprehensivePerformance = netPerformance + jiaban * 0.01;
        BigDecimal bd = new BigDecimal(comprehensivePerformance);
        return bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public void setComprehensivePerformance(double comprehensivePerformance) {
        this.comprehensivePerformance = comprehensivePerformance;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getLaowupaiqian() {
        return laowupaiqian;
    }

    public void setLaowupaiqian(String laowupaiqian) {
        this.laowupaiqian = laowupaiqian;
    }

    public int getBasicwages() {
        return basicwages;
    }

    public void setBasicwages(int basicwages) {
        this.basicwages = basicwages;
    }

    public double getMeritpay() {
        BigDecimal bd1 = new BigDecimal(comprehensivePerformance);
        BigDecimal bd = new BigDecimal(meritpay * bd1.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
        return bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public void setMeritpay(int meritpay) {
        this.meritpay = meritpay;
    }

    public double getMealSupplement() {
        mealSupplement = kaoqin * canbuxishu;
        return mealSupplement;
    }

    public void setMealSupplement(double mealSupplement) {
        this.mealSupplement = mealSupplement;
    }

    public int getPhoneAllowance() {
        return phoneAllowance;
    }

    public void setPhoneAllowance(int phoneAllowance) {
        this.phoneAllowance = phoneAllowance;
    }

    public double getSalaryJiaban() {
        salaryJiaban = jiaban * jiabanxishu;
        return salaryJiaban;
    }

    public void setSalaryJiaban(double salaryJiaban) {
        this.salaryJiaban = salaryJiaban;
    }

    public double getWagePayable() {
        BigDecimal bd1 = new BigDecimal(comprehensivePerformance);
        double aa = bd1.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        wagePayable = basicwages + phoneAllowance + mealSupplement + meritpay * aa + salaryJiaban;
        BigDecimal bd = new BigDecimal(wagePayable);
        return bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public void setWagePayable(double wagePayable) {
        this.wagePayable = wagePayable;
    }

    public double getTax1() {
//        tax1 = wagePayable -5000 - yanglao - yiliao - shiye - zhuanxiang -qitakouchu -gongjijin;
        //我也不知道为什么 但是这里的wagePayable就是取不对！
        tax1 = wagePayable - 5000 - yanglao - yiliao - shiye - zhuanxiang - qitakouchu - gongjijin;
        return tax1;
    }

    public void setTax1(double tax1) {
        this.tax1 = tax1;
    }

    public double getTaxTotal() {
        return taxTotal;
    }

    public void setTaxTotal(double taxTotal) {
        this.taxTotal = taxTotal;
    }

    public double getTaxTotal2() {
        return taxTotal2;
    }

    public void setTaxTotal2(double taxTotal2) {
        this.taxTotal2 = taxTotal2;
    }


}
