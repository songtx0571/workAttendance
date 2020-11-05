package com.howei.pojo;

import java.io.Serializable;

/**
 * 税
 */
public class Tax implements Serializable {
    private Integer id;
    private Integer employeeId;//员工id
    private Double currentIncome;//本期收入
    private Double fiveInsurancesAndOneFund;//五险一金
    private Double specialAdditionalDeduction;//专项扣除
    private Double deductionOfExpenses;//减除费用
    private Double personalIncomeTax;//个税
    private Double other;//其他
    private String date;//时间
    private String created;//创建时间

    @Override
    public String toString() {
        return "Tax{" +
                "id=" + id +
                ", employeeId=" + employeeId +
                ", currentIncome='" + currentIncome + '\'' +
                ", fiveInsurancesAndOneFund=" + fiveInsurancesAndOneFund +
                ", specialAdditionalDeduction=" + specialAdditionalDeduction +
                ", deductionOfExpenses=" + deductionOfExpenses +
                ", other=" + other +
                ", date='" + date + '\'' +
                ", created='" + created + '\'' +
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

    public Double getCurrentIncome() {
        return currentIncome;
    }

    public void setCurrentIncome(Double currentIncome) {
        this.currentIncome = currentIncome;
    }

    public Double getFiveInsurancesAndOneFund() {
        return fiveInsurancesAndOneFund;
    }

    public void setFiveInsurancesAndOneFund(Double fiveInsurancesAndOneFund) {
        this.fiveInsurancesAndOneFund = fiveInsurancesAndOneFund;
    }

    public Double getSpecialAdditionalDeduction() {
        return specialAdditionalDeduction;
    }

    public void setSpecialAdditionalDeduction(Double specialAdditionalDeduction) {
        this.specialAdditionalDeduction = specialAdditionalDeduction;
    }

    public Double getDeductionOfExpenses() {
        return deductionOfExpenses;
    }

    public void setDeductionOfExpenses(Double deductionOfExpenses) {
        this.deductionOfExpenses = deductionOfExpenses;
    }

    public Double getOther() {
        return other;
    }

    public void setOther(Double other) {
        this.other = other;
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

    public Double getPersonalIncomeTax() {
        return personalIncomeTax;
    }

    public void setPersonalIncomeTax(Double personalIncomeTax) {
        this.personalIncomeTax = personalIncomeTax;
    }
}
