package com.howei.pojo;

import java.io.Serializable;

/**
 * 工资明细
 */
public class Wages implements Serializable {
    private Integer id;
    private Integer employeeId;
    private double basePay;//基本工资 岗位工资
    private double skillPay;//技能工资
    private double positionSalary;//职务工资 职级工资
    private double seniorityWage;//工龄工资
    private double other;//其他
    private double meritBase;//绩效基数
    private double meritPay;//绩效工资
    private double wageSubtotal;//工资小记
    private double performanceCoefficient;//绩效系数:3位有效小数
    private double wagesPayable;//应发工资
    private double foodSupplement;//餐补
    private double totalPayable;//应发合计:本期收入
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
    private double highTemperatureSubsidy;//高温补贴
    private double subTotalOfSubsidies;//补贴小计
    private Integer postGradeId;//岗位等级Id
    private Integer wagesPostId;//工资岗位Id
    private double specialAdditionalDeduction;//专项附加扣除
    private double incomeTotal;//累计收入额
    private double deductionOfExpensesTaxTotal;//累计费用减免
    private double specialDeductionTaxTotal;//累计专项扣除
    private double specialAdditionalDeductionTaxTotal;//累计附加专项扣除
    private double otherDeductionTaxTotal;//累计其他扣除
    private double taxableIncomeTotal;//累计应缴纳税所得额
    private double individualIncomeTaxTotal;//累计个税
    private double individualIncomeTaxPaidTotal;//累计已缴纳个税
    private double totalTaxTotal;//累计计税合计
    private double overtimeSubsidy;//加班补贴
    private double jiaban;//加班工时
    //虚字段
    private String employeeName;//员工姓名
    private String userNumber;//员工姓名
    private String laowupaiqian;//劳务派遣
    private String departmentName;//部门名称
    private String isChanged;//人事异动
    private String postGradeName;//岗位等级名称
    private String wagesPostName;//薪资岗位名称
    private int workAttendance;//考勤天数

    public static Wages initTotalValue(Integer employeeId) {
        Wages wages = new Wages();
        wages.setEmployeeId(employeeId);
        wages.setIncomeTotal(0.00);
        wages.setDeductionOfExpensesTaxTotal(0.00);
        wages.setSpecialDeductionTaxTotal(0.00);
        wages.setSpecialAdditionalDeductionTaxTotal(0.00);
        wages.setOtherDeductionTaxTotal(0.00);
        wages.setTaxableIncomeTotal(0.00);
        wages.setIndividualIncomeTaxTotal(0.00);
        wages.setIndividualIncomeTaxPaidTotal(0.00);
        wages.setTotalTaxTotal(0.00);
        return wages;
    }

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
                ", meritBase=" + meritBase +
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
                ", subTotalOfSubsidies=" + subTotalOfSubsidies +
                ", postGradeId=" + postGradeId +
                ", wagesPostId=" + wagesPostId +
                ", specialAdditionalDeduction=" + specialAdditionalDeduction +
                ", incomeTotal=" + incomeTotal +
                ", deductionOfExpensesTaxTotal=" + deductionOfExpensesTaxTotal +
                ", specialDeductionTaxTotal=" + specialDeductionTaxTotal +
                ", specialAdditionalDeductionTaxTotal=" + specialAdditionalDeductionTaxTotal +
                ", otherDeductionTaxTotal=" + otherDeductionTaxTotal +
                ", taxableIncomeTotal=" + taxableIncomeTotal +
                ", individualIncomeTaxTotal=" + individualIncomeTaxTotal +
                ", individualIncomeTaxPaidTotal=" + individualIncomeTaxPaidTotal +
                ", totalTaxTotal=" + totalTaxTotal +
                ", overtimeSubsidy=" + overtimeSubsidy +
                ", employeeName='" + employeeName + '\'' +
                ", userNumber='" + userNumber + '\'' +
                ", laowupaiqian='" + laowupaiqian + '\'' +
                ", departmentName='" + departmentName + '\'' +
                ", isChanged='" + isChanged + '\'' +
                ", postGradeName='" + postGradeName + '\'' +
                ", wagesPostName='" + wagesPostName + '\'' +
                ", jiaban=" + jiaban +
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

    public double getMeritBase() {
        return meritBase;
    }

    public void setMeritBase(double meritBase) {
        this.meritBase = meritBase;
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

    public double getHighTemperatureSubsidy() {
        return highTemperatureSubsidy;
    }

    public void setHighTemperatureSubsidy(double highTemperatureSubsidy) {
        this.highTemperatureSubsidy = highTemperatureSubsidy;
    }

    public double getSubTotalOfSubsidies() {
        return subTotalOfSubsidies;
    }

    public void setSubTotalOfSubsidies(double subTotalOfSubsidies) {
        this.subTotalOfSubsidies = subTotalOfSubsidies;
    }

    public Integer getPostGradeId() {
        return postGradeId;
    }

    public void setPostGradeId(Integer postGradeId) {
        this.postGradeId = postGradeId;
    }

    public Integer getWagesPostId() {
        return wagesPostId;
    }

    public void setWagesPostId(Integer wagesPostId) {
        this.wagesPostId = wagesPostId;
    }

    public double getSpecialAdditionalDeduction() {
        return specialAdditionalDeduction;
    }

    public void setSpecialAdditionalDeduction(double specialAdditionalDeduction) {
        this.specialAdditionalDeduction = specialAdditionalDeduction;
    }

    public double getIncomeTotal() {
        return incomeTotal;
    }

    public void setIncomeTotal(double incomeTotal) {
        this.incomeTotal = incomeTotal;
    }

    public double getDeductionOfExpensesTaxTotal() {
        return deductionOfExpensesTaxTotal;
    }

    public void setDeductionOfExpensesTaxTotal(double deductionOfExpensesTaxTotal) {
        this.deductionOfExpensesTaxTotal = deductionOfExpensesTaxTotal;
    }

    public double getSpecialDeductionTaxTotal() {
        return specialDeductionTaxTotal;
    }

    public void setSpecialDeductionTaxTotal(double specialDeductionTaxTotal) {
        this.specialDeductionTaxTotal = specialDeductionTaxTotal;
    }

    public double getSpecialAdditionalDeductionTaxTotal() {
        return specialAdditionalDeductionTaxTotal;
    }

    public void setSpecialAdditionalDeductionTaxTotal(double specialAdditionalDeductionTaxTotal) {
        this.specialAdditionalDeductionTaxTotal = specialAdditionalDeductionTaxTotal;
    }

    public double getOtherDeductionTaxTotal() {
        return otherDeductionTaxTotal;
    }

    public void setOtherDeductionTaxTotal(double otherDeductionTaxTotal) {
        this.otherDeductionTaxTotal = otherDeductionTaxTotal;
    }

    public double getTaxableIncomeTotal() {
        return taxableIncomeTotal;
    }

    public void setTaxableIncomeTotal(double taxableIncomeTotal) {
        this.taxableIncomeTotal = taxableIncomeTotal;
    }

    public double getIndividualIncomeTaxTotal() {
        return individualIncomeTaxTotal;
    }

    public void setIndividualIncomeTaxTotal(double individualIncomeTaxTotal) {
        this.individualIncomeTaxTotal = individualIncomeTaxTotal;
    }

    public double getIndividualIncomeTaxPaidTotal() {
        return individualIncomeTaxPaidTotal;
    }

    public void setIndividualIncomeTaxPaidTotal(double individualIncomeTaxPaidTotal) {
        this.individualIncomeTaxPaidTotal = individualIncomeTaxPaidTotal;
    }

    public double getTotalTaxTotal() {
        return totalTaxTotal;
    }

    public void setTotalTaxTotal(double totalTaxTotal) {
        this.totalTaxTotal = totalTaxTotal;
    }

    public double getOvertimeSubsidy() {
        return overtimeSubsidy;
    }

    public void setOvertimeSubsidy(double overtimeSubsidy) {
        this.overtimeSubsidy = overtimeSubsidy;
    }

    public double getJiaban() {
        return jiaban;
    }

    public void setJiaban(double jiaban) {
        this.jiaban = jiaban;
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

    public String getLaowupaiqian() {
        return laowupaiqian;
    }

    public void setLaowupaiqian(String laowupaiqian) {
        this.laowupaiqian = laowupaiqian;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getIsChanged() {
        return isChanged;
    }

    public void setIsChanged(String isChanged) {
        this.isChanged = isChanged;
    }

    public String getPostGradeName() {
        return postGradeName;
    }

    public void setPostGradeName(String postGradeName) {
        this.postGradeName = postGradeName;
    }

    public String getWagesPostName() {
        return wagesPostName;
    }

    public void setWagesPostName(String wagesPostName) {
        this.wagesPostName = wagesPostName;
    }

    public int getWorkAttendance() {
        return workAttendance;
    }

    public void setWorkAttendance(int workAttendance) {
        this.workAttendance = workAttendance;
    }
}
