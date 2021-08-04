package com.howei.pojo;

import java.io.Serializable;

/**
 * 工资明细
 */
public class Wages implements Serializable {
    private Integer id;
    private Integer employeeId;
    private Double basePay;//基本工资 岗位工资
    private Double skillPay;//技能工资
    private Double positionSalary;//职务工资 职级工资
    private Double seniorityWage;//工龄工资
    private Double other;//其他
    private Double meritBase;//绩效基数
    private Double meritPay;//绩效工资
    private Double wageSubtotal;//工资小记
    private Double performanceCoefficient;//绩效系数:3位有效小数
    private Double wagesPayable;//应发工资
    private Double foodSupplement;//餐补
    private Double totalPayable;//应发合计:本期收入
    private Double endowmentInsurance;//养老保险
    private Double unemploymentBenefits;//失业金
    private Double medicalInsurance;//医疗保险
    private Double accumulationFund;//公积金
    private Double otherDeductions;//其他扣款
    private Double unionFees;//工会费
    private Double totalDeduction;//扣款合计
    private Double totalTax;//计税合计
    private Double sixSpecialDeductions;//六项专项扣除金额
    private Double individualTaxAdjustment;//个调税
    private Double netSalary;//实发工资
    private String remark;//备注
    private String date;//日期
    private String created;//创建时间
    private Double highTemperatureSubsidy;//高温补贴
    private Double subTotalOfSubsidies;//补贴小计
    private Integer postGradeId;//岗位等级Id
    private Integer wagesPostId;//工资岗位Id
    private Double specialAdditionalDeduction;//专项附加扣除
    private Double incomeTotal;//累计收入额
    private Double deductionOfExpensesTaxTotal;//累计费用减免
    private Double specialDeductionTaxTotal;//累计专项扣除
    private Double specialAdditionalDeductionTaxTotal;//累计附加专项扣除
    private Double otherDeductionTaxTotal;//累计其他扣除
    private Double taxableIncomeTotal;//累计应缴纳税所得额
    private Double individualIncomeTaxTotal;//累计个税
    private Double individualIncomeTaxPaidTotal;//累计已缴纳个税
    private Double totalTaxTotal;//累计计税合计
    private Double overtimeSubsidy;//加班补贴

    //虚字段
    private String employeeName;//员工姓名
    private String userNumber;//员工姓名
    private String laowupaiqian;//劳务派遣
    private String departmentName;//部门名称
    private String isChanged;//人事异动
    private String postGradeName;//岗位等级名称
    private String wagesPostName;//薪资岗位名称
    private Double jiaban;//加班工时

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

    public Double getJiaban() {
        return jiaban;
    }

    public void setJiaban(Double jiaban) {
        this.jiaban = jiaban;
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

    public Double getBasePay() {
        return basePay;
    }

    public void setBasePay(Double basePay) {
        this.basePay = basePay;
    }

    public Double getSkillPay() {
        return skillPay;
    }

    public void setSkillPay(Double skillPay) {
        this.skillPay = skillPay;
    }

    public Double getPositionSalary() {
        return positionSalary;
    }

    public void setPositionSalary(Double positionSalary) {
        this.positionSalary = positionSalary;
    }

    public Double getSeniorityWage() {
        return seniorityWage;
    }

    public void setSeniorityWage(Double seniorityWage) {
        this.seniorityWage = seniorityWage;
    }

    public Double getOther() {
        return other;
    }

    public void setOther(Double other) {
        this.other = other;
    }

    public Double getMeritBase() {
        return meritBase;
    }

    public void setMeritBase(Double meritBase) {
        this.meritBase = meritBase;
    }

    public Double getMeritPay() {
        return meritPay;
    }

    public void setMeritPay(Double meritPay) {
        this.meritPay = meritPay;
    }

    public Double getWageSubtotal() {
        return wageSubtotal;
    }

    public void setWageSubtotal(Double wageSubtotal) {
        this.wageSubtotal = wageSubtotal;
    }

    public Double getPerformanceCoefficient() {
        return performanceCoefficient;
    }

    public void setPerformanceCoefficient(Double performanceCoefficient) {
        this.performanceCoefficient = performanceCoefficient;
    }

    public Double getWagesPayable() {
        return wagesPayable;
    }

    public void setWagesPayable(Double wagesPayable) {
        this.wagesPayable = wagesPayable;
    }

    public Double getFoodSupplement() {
        return foodSupplement;
    }

    public void setFoodSupplement(Double foodSupplement) {
        this.foodSupplement = foodSupplement;
    }

    public Double getTotalPayable() {
        return totalPayable;
    }

    public void setTotalPayable(Double totalPayable) {
        this.totalPayable = totalPayable;
    }

    public Double getEndowmentInsurance() {
        return endowmentInsurance;
    }

    public void setEndowmentInsurance(Double endowmentInsurance) {
        this.endowmentInsurance = endowmentInsurance;
    }

    public Double getUnemploymentBenefits() {
        return unemploymentBenefits;
    }

    public void setUnemploymentBenefits(Double unemploymentBenefits) {
        this.unemploymentBenefits = unemploymentBenefits;
    }

    public Double getMedicalInsurance() {
        return medicalInsurance;
    }

    public void setMedicalInsurance(Double medicalInsurance) {
        this.medicalInsurance = medicalInsurance;
    }

    public Double getAccumulationFund() {
        return accumulationFund;
    }

    public void setAccumulationFund(Double accumulationFund) {
        this.accumulationFund = accumulationFund;
    }

    public Double getOtherDeductions() {
        return otherDeductions;
    }

    public void setOtherDeductions(Double otherDeductions) {
        this.otherDeductions = otherDeductions;
    }

    public Double getUnionFees() {
        return unionFees;
    }

    public void setUnionFees(Double unionFees) {
        this.unionFees = unionFees;
    }

    public Double getTotalDeduction() {
        return totalDeduction;
    }

    public void setTotalDeduction(Double totalDeduction) {
        this.totalDeduction = totalDeduction;
    }

    public Double getTotalTax() {
        return totalTax;
    }

    public void setTotalTax(Double totalTax) {
        this.totalTax = totalTax;
    }

    public Double getSixSpecialDeductions() {
        return sixSpecialDeductions;
    }

    public void setSixSpecialDeductions(Double sixSpecialDeductions) {
        this.sixSpecialDeductions = sixSpecialDeductions;
    }

    public Double getIndividualTaxAdjustment() {
        return individualTaxAdjustment;
    }

    public void setIndividualTaxAdjustment(Double individualTaxAdjustment) {
        this.individualTaxAdjustment = individualTaxAdjustment;
    }

    public Double getNetSalary() {
        return netSalary;
    }

    public void setNetSalary(Double netSalary) {
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

    public Double getSubTotalOfSubsidies() {
        return subTotalOfSubsidies;
    }

    public void setSubTotalOfSubsidies(Double subTotalOfSubsidies) {
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

    public Double getSpecialAdditionalDeduction() {
        return specialAdditionalDeduction;
    }

    public void setSpecialAdditionalDeduction(Double specialAdditionalDeduction) {
        this.specialAdditionalDeduction = specialAdditionalDeduction;
    }

    public Double getIncomeTotal() {
        return incomeTotal;
    }

    public void setIncomeTotal(Double incomeTotal) {
        this.incomeTotal = incomeTotal;
    }

    public Double getDeductionOfExpensesTaxTotal() {
        return deductionOfExpensesTaxTotal;
    }

    public void setDeductionOfExpensesTaxTotal(Double deductionOfExpensesTaxTotal) {
        this.deductionOfExpensesTaxTotal = deductionOfExpensesTaxTotal;
    }

    public Double getSpecialDeductionTaxTotal() {
        return specialDeductionTaxTotal;
    }

    public void setSpecialDeductionTaxTotal(Double specialDeductionTaxTotal) {
        this.specialDeductionTaxTotal = specialDeductionTaxTotal;
    }

    public Double getSpecialAdditionalDeductionTaxTotal() {
        return specialAdditionalDeductionTaxTotal;
    }

    public void setSpecialAdditionalDeductionTaxTotal(Double specialAdditionalDeductionTaxTotal) {
        this.specialAdditionalDeductionTaxTotal = specialAdditionalDeductionTaxTotal;
    }

    public Double getOtherDeductionTaxTotal() {
        return otherDeductionTaxTotal;
    }

    public void setOtherDeductionTaxTotal(Double otherDeductionTaxTotal) {
        this.otherDeductionTaxTotal = otherDeductionTaxTotal;
    }

    public Double getTaxableIncomeTotal() {
        return taxableIncomeTotal;
    }

    public void setTaxableIncomeTotal(Double taxableIncomeTotal) {
        this.taxableIncomeTotal = taxableIncomeTotal;
    }

    public Double getIndividualIncomeTaxTotal() {
        return individualIncomeTaxTotal;
    }

    public void setIndividualIncomeTaxTotal(Double individualIncomeTaxTotal) {
        this.individualIncomeTaxTotal = individualIncomeTaxTotal;
    }

    public Double getIndividualIncomeTaxPaidTotal() {
        return individualIncomeTaxPaidTotal;
    }

    public void setIndividualIncomeTaxPaidTotal(Double individualIncomeTaxPaidTotal) {
        this.individualIncomeTaxPaidTotal = individualIncomeTaxPaidTotal;
    }

    public Double getTotalTaxTotal() {
        return totalTaxTotal;
    }

    public void setTotalTaxTotal(Double totalTaxTotal) {
        this.totalTaxTotal = totalTaxTotal;
    }

    public Double getOvertimeSubsidy() {
        return overtimeSubsidy;
    }

    public void setOvertimeSubsidy(Double overtimeSubsidy) {
        this.overtimeSubsidy = overtimeSubsidy;
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
}
