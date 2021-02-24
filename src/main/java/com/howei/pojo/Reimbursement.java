package com.howei.pojo;

/**
 * 报销单
 */
public class Reimbursement {
    private Integer id;
    private Integer reimbursor;//报销人
    private Integer departmentId;//部门
    private String reimbursementContent;//报销内容
    private Integer subject;//科目
    private double reimbursementAmount;//报销金额
    private String remark;//备注
    private String reimbursementDate;//报销日期
    private Integer createdBy;
    private String created;
    private Integer status;//审核状态:0未审核；1审核中；2审核结束
    private Integer financeResult;//财务审核结果：1确认有效；2确认无效；3审核通过；4审核未通过
    private String financeDate;//财务审核日期
    private String financeRemark;//财务审核意见

    private String reimbursorName;//报销人名称
    private String departemntName;//部门名称
    private String createdByName;//创建人名称
    private Integer companyId;//公司id

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getReimbursor() {
        return reimbursor;
    }

    public void setReimbursor(Integer reimbursor) {
        this.reimbursor = reimbursor;
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    public String getReimbursementContent() {
        return reimbursementContent;
    }

    public void setReimbursementContent(String reimbursementContent) {
        this.reimbursementContent = reimbursementContent;
    }

    public Integer getSubject() {
        return subject;
    }

    public void setSubject(Integer subject) {
        this.subject = subject;
    }

    public double getReimbursementAmount() {
        return reimbursementAmount;
    }

    public void setReimbursementAmount(double reimbursementAmount) {
        this.reimbursementAmount = reimbursementAmount;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getReimbursementDate() {
        return reimbursementDate;
    }

    public void setReimbursementDate(String reimbursementDate) {
        this.reimbursementDate = reimbursementDate;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getFinanceResult() {
        return financeResult;
    }

    public void setFinanceResult(Integer financeResult) {
        this.financeResult = financeResult;
    }

    public String getFinanceDate() {
        return financeDate;
    }

    public void setFinanceDate(String financeDate) {
        this.financeDate = financeDate;
    }

    public String getFinanceRemark() {
        return financeRemark;
    }

    public void setFinanceRemark(String financeRemark) {
        this.financeRemark = financeRemark;
    }

    public String getReimbursorName() {
        return reimbursorName;
    }

    public void setReimbursorName(String reimbursorName) {
        this.reimbursorName = reimbursorName;
    }

    public String getDepartemntName() {
        return departemntName;
    }

    public void setDepartemntName(String departemntName) {
        this.departemntName = departemntName;
    }

    public String getCreatedByName() {
        return createdByName;
    }

    public void setCreatedByName(String createdByName) {
        this.createdByName = createdByName;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }
}
