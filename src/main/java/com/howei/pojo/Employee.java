package com.howei.pojo;

import java.io.Serializable;
import java.util.List;

//员工信息
public class Employee implements Serializable {
    private int id;
    private String name;//姓名
    private String userNumber;//编号
    private String idNumber;//身份证
    private String bank;//开户行
    private String card;//银行卡
    private String phone;//手机
    private String closhe;//衣服尺寸
    private String hat;//安全帽编号
    private String laowupaiqian;//劳务派遣
    private String education;//学历
    private String credentials1;//专业1
    private String credentials2;//专业2
    private String credentials3;//专业3
    private String entryDate;//入职日期
    private String poision;//
    private String emergency;//应急联系人
    private String emergencyTel;//应急手机
    private String remark;//备注
    private String wages;//待遇标准
    private String basicwages;//基本工资
    private String meritpay;//绩效工资
    private String address;//住址
    private Integer manager;//绩效管理人
    private String created;
    private int createdBy;
    private String sign;//个性签名
    private String email;//邮箱

    private Double skillPay;//技能工资
    private Double positionSalary;//职务工资
    private Double seniorityWage;//工龄工资
    private Double foodSupplement;//餐补
    private Double endowmentInsurance;//养老保险
    private Double unemploymentBenefits;//失业金
    private Double medicalInsurance;//医疗保险
    private Double accumulationFund;//公积金
    private Double unionFees;//工会费

    //虚字段
    private String stateName;
    private String sexName;
    private String departmentName;
    private String postName;//岗位
    private String companyName;
    private String managerName;//绩效管理人
    private List<Employee> managerList;//被绩效管理人
    private String isChanged;//人事异动

    public String getIsChanged() {
        return isChanged;
    }

    public void setIsChanged(String isChanged) {
        this.isChanged = isChanged;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserNumber() {
        return userNumber;
    }

    public void setUserNumber(String userNumber) {
        this.userNumber = userNumber;
    }

    public String getSexName() {
        return sexName;
    }

    public void setSexName(String sexName) {
        this.sexName = sexName;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getCard() {
        return card;
    }

    public void setCard(String card) {
        this.card = card;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCloshe() {
        return closhe;
    }

    public void setCloshe(String closhe) {
        this.closhe = closhe;
    }

    public String getHat() {
        return hat;
    }

    public void setHat(String hat) {
        this.hat = hat;
    }

    public String getLaowupaiqian() {
        return laowupaiqian;
    }

    public void setLaowupaiqian(String laowupaiqian) {
        this.laowupaiqian = laowupaiqian;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getCredentials1() {
        return credentials1;
    }

    public void setCredentials1(String credentials1) {
        this.credentials1 = credentials1;
    }

    public String getCredentials2() {
        return credentials2;
    }

    public void setCredentials2(String credentials2) {
        this.credentials2 = credentials2;
    }

    public String getCredentials3() {
        return credentials3;
    }

    public void setCredentials3(String credentials3) {
        this.credentials3 = credentials3;
    }

    public String getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(String entryDate) {
        this.entryDate = entryDate;
    }

    public String getPoision() {
        return poision;
    }

    public void setPoision(String poision) {
        this.poision = poision;
    }

    public String getEmergency() {
        return emergency;
    }

    public void setEmergency(String emergency) {
        this.emergency = emergency;
    }

    public String getEmergencyTel() {
        return emergencyTel;
    }

    public void setEmergencyTel(String emergencyTel) {
        this.emergencyTel = emergencyTel;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getWages() {
        return wages;
    }

    public void setWages(String wages) {
        this.wages = wages;
    }

    public String getBasicwages() {
        return basicwages;
    }

    public void setBasicwages(String basicwages) {
        this.basicwages = basicwages;
    }

    public String getMeritpay() {
        return meritpay;
    }

    public void setMeritpay(String meritpay) {
        this.meritpay = meritpay;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getPostName() {
        return postName;
    }

    public void setPostName(String postName) {
        this.postName = postName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCreated() {
        return created;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public int getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public Integer getManager() {
        return manager;
    }

    public void setManager(Integer manager) {
        this.manager = manager;
    }

    public List<Employee> getManagerList() {
        return managerList;
    }

    public void setManagerList(List<Employee> managerList) {
        this.managerList = managerList;
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

    public Double getFoodSupplement() {
        return foodSupplement;
    }

    public void setFoodSupplement(Double foodSupplement) {
        this.foodSupplement = foodSupplement;
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

    public Double getUnionFees() {
        return unionFees;
    }

    public void setUnionFees(Double unionFees) {
        this.unionFees = unionFees;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", userNumber='" + userNumber + '\'' +
                ", idNumber='" + idNumber + '\'' +
                ", bank='" + bank + '\'' +
                ", card='" + card + '\'' +
                ", phone='" + phone + '\'' +
                ", closhe='" + closhe + '\'' +
                ", hat='" + hat + '\'' +
                ", laowupaiqian='" + laowupaiqian + '\'' +
                ", education='" + education + '\'' +
                ", credentials1='" + credentials1 + '\'' +
                ", credentials2='" + credentials2 + '\'' +
                ", credentials3='" + credentials3 + '\'' +
                ", entryDate='" + entryDate + '\'' +
                ", poision='" + poision + '\'' +
                ", emergency='" + emergency + '\'' +
                ", emergencyTel='" + emergencyTel + '\'' +
                ", remark='" + remark + '\'' +
                ", wages='" + wages + '\'' +
                ", basicwages='" + basicwages + '\'' +
                ", meritpay='" + meritpay + '\'' +
                ", address='" + address + '\'' +
                ", manager=" + manager +
                ", created='" + created + '\'' +
                ", createdBy=" + createdBy +
                ", sign='" + sign + '\'' +
                ", email='" + email + '\'' +
                ", skillPay=" + skillPay +
                ", positionSalary=" + positionSalary +
                ", seniorityWage=" + seniorityWage +
                ", foodSupplement=" + foodSupplement +
                ", endowmentInsurance=" + endowmentInsurance +
                ", unemploymentBenefits=" + unemploymentBenefits +
                ", medicalInsurance=" + medicalInsurance +
                ", accumulationFund=" + accumulationFund +
                ", unionFees=" + unionFees +
                ", stateName='" + stateName + '\'' +
                ", sexName='" + sexName + '\'' +
                ", departmentName='" + departmentName + '\'' +
                ", postName='" + postName + '\'' +
                ", companyName='" + companyName + '\'' +
                ", managerName='" + managerName + '\'' +
                ", managerList=" + managerList +
                ", isChanged='" + isChanged + '\'' +
                '}';
    }
}
