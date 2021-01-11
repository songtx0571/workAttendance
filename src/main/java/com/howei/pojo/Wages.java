package com.howei.pojo;

import java.io.Serializable;

/**
 * 工资明细
 */
public class Wages implements Serializable {
    private Integer id;
    private Integer employeeId;
    private double basePay;//基本工资
    private double skillPay;//技能工资
    private double positionSalary;//职务工资
    private double seniorityWage;//工龄工资
    private double other;//其他
    private double meritPay;//绩效工资
    private double wageSubtotal;//工资小记
    private double performanceCoefficient;//绩效系数:3位有效小数
    private double wagesPayable;//应发工资
    private double foodSupplement;//餐补
    private double totalPayable;//应发合计
    private double endowmentInsurance;//养老保险
    private double unemploymentBenefits;//失业金
    private double medicalInsurance;//医疗保险
    private double accumulationFund;//公积金
    private double otherDeductions;//其他扣款
    private double unionFees;//工会费
    private double totalDeduction;//扣款合计
    private double totalTax;//计税合计
    private double sixSpecialDeductions;//六项专项扣除金额
    private double individualTaxAdjustment;//个调税
    private double netSalary;//实发工资
    private String remark;//备注
    private String date;//日期
    private String created;//创建时间
    private Double highTemperatureSubsidy;//高温补贴

    //虚字段
    private String employeeName;//员工姓名
    private String userNumber;//员工姓名

    @Override
    public String toString() {
        return "Wages{" +
                "id=" + id +
                ", employeeId=" + employeeId +
                ", basePay=" + basePay +
                ", skillPay=" + skillPay +
                ", positionSalary=" + positionSalary +
                ", seniorityWage=" + seniorityWage +
                ", other=" + other +
                ", meritPay=" + meritPay +
                ", wageSubtotal=" + wageSubtotal +
                ", performanceCoefficient=" + performanceCoefficient +
                ", wagesPayable=" + wagesPayable +
                ", foodSupplement=" + foodSupplement +
                ", totalPayable=" + totalPayable +
                ", endowmentInsurance=" + endowmentInsurance +
                ", unemploymentBenefits=" + unemploymentBenefits +
                ", medicalInsurance=" + medicalInsurance +
                ", accumulationFund=" + accumulationFund +
                ", otherDeductions=" + otherDeductions +
                ", unionFees=" + unionFees +
                ", totalDeduction=" + totalDeduction +
                ", totalTax=" + totalTax +
                ", sixSpecialDeductions=" + sixSpecialDeductions +
                ", individualTaxAdjustment=" + individualTaxAdjustment +
                ", netSalary=" + netSalary +
                ", remark='" + remark + '\'' +
                ", date='" + date + '\'' +
                ", created='" + created + '\'' +
                ", highTemperatureSubsidy=" + highTemperatureSubsidy +
                ", employeeName='" + employeeName + '\'' +
                ", userNumber='" + userNumber + '\'' +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public double getBasePay() {
        return basePay;
    }

    public void setBasePay(double basePay) {
        this.basePay = basePay;
    }

    public double getSkillPay() {
        return skillPay;
    }

    public void setSkillPay(double skillPay) {
        this.skillPay = skillPay;
    }

    public double getPositionSalary() {
        return positionSalary;
    }

    public void setPositionSalary(double positionSalary) {
        this.positionSalary = positionSalary;
    }

    public double getSeniorityWage() {
        return seniorityWage;
    }

    public void setSeniorityWage(double seniorityWage) {
        this.seniorityWage = seniorityWage;
    }

    public double getOther() {
        return other;
    }

    public void setOther(double other) {
        this.other = other;
    }

    public double getMeritPay() {
        return meritPay;
    }

    public void setMeritPay(double meritPay) {
        this.meritPay = meritPay;
    }

    public double getWageSubtotal() {
        return wageSubtotal;
    }

    public void setWageSubtotal(double wageSubtotal) {
        this.wageSubtotal = wageSubtotal;
    }

    public double getPerformanceCoefficient() {
        return performanceCoefficient;
    }

    public void setPerformanceCoefficient(double performanceCoefficient) {
        this.performanceCoefficient = performanceCoefficient;
    }

    public double getWagesPayable() {
        return wagesPayable;
    }

    public void setWagesPayable(double wagesPayable) {
        this.wagesPayable = wagesPayable;
    }

    public double getFoodSupplement() {
        return foodSupplement;
    }

    public void setFoodSupplement(double foodSupplement) {
        this.foodSupplement = foodSupplement;
    }

    public double getTotalPayable() {
        return totalPayable;
    }

    public void setTotalPayable(double totalPayable) {
        this.totalPayable = totalPayable;
    }

    public double getEndowmentInsurance() {
        return endowmentInsurance;
    }

    public void setEndowmentInsurance(double endowmentInsurance) {
        this.endowmentInsurance = endowmentInsurance;
    }

    public double getUnemploymentBenefits() {
        return unemploymentBenefits;
    }

    public void setUnemploymentBenefits(double unemploymentBenefits) {
        this.unemploymentBenefits = unemploymentBenefits;
    }

    public double getMedicalInsurance() {
        return medicalInsurance;
    }

    public void setMedicalInsurance(double medicalInsurance) {
        this.medicalInsurance = medicalInsurance;
    }

    public double getAccumulationFund() {
        return accumulationFund;
    }

    public void setAccumulationFund(double accumulationFund) {
        this.accumulationFund = accumulationFund;
    }

    public double getOtherDeductions() {
        return otherDeductions;
    }

    public void setOtherDeductions(double otherDeductions) {
        this.otherDeductions = otherDeductions;
    }

    public double getUnionFees() {
        return unionFees;
    }

    public void setUnionFees(double unionFees) {
        this.unionFees = unionFees;
    }

    public double getTotalDeduction() {
        return totalDeduction;
    }

    public void setTotalDeduction(double totalDeduction) {
        this.totalDeduction = totalDeduction;
    }

    public double getTotalTax() {
        return totalTax;
    }

    public void setTotalTax(double totalTax) {
        this.totalTax = totalTax;
    }

    public double getSixSpecialDeductions() {
        return sixSpecialDeductions;
    }

    public void setSixSpecialDeductions(double sixSpecialDeductions) {
        this.sixSpecialDeductions = sixSpecialDeductions;
    }

    public double getIndividualTaxAdjustment() {
        return individualTaxAdjustment;
    }

    public void setIndividualTaxAdjustment(double individualTaxAdjustment) {
        this.individualTaxAdjustment = individualTaxAdjustment;
    }

    public double getNetSalary() {
        return netSalary;
    }

    public void setNetSalary(double netSalary) {
        this.netSalary = netSalary;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public Double getHighTemperatureSubsidy() {
        return highTemperatureSubsidy;
    }

    public void setHighTemperatureSubsidy(Double highTemperatureSubsidy) {
        this.highTemperatureSubsidy = highTemperatureSubsidy;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getUserNumber() {
        return userNumber;
    }

    public void setUserNumber(String userNumber) {
        this.userNumber = userNumber;
    }
}
