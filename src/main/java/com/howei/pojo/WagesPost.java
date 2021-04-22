package com.howei.pojo;

import java.util.List;


/**
 * 工资岗位
 */
public class WagesPost {

    private Integer id;
    private String name;
    private Integer companyId;//公司id
    private Integer isactive;
    private double wagesPostWage;//薪资
    private List<PostGrade> postGrade;//岗位等级列表

    //虚字段index
    private Integer index;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Integer getIsactive() {
        return isactive;
    }

    public void setIsactive(Integer isactive) {
        this.isactive = isactive;
    }

    public List<PostGrade> getPostGrade() {
        return postGrade;
    }

    public void setPostGrade(List<PostGrade> postGrade) {
        this.postGrade = postGrade;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public double getWagesPostWage() {
        return wagesPostWage;
    }

    public void setWagesPostWage(double wagesPostWage) {
        this.wagesPostWage = wagesPostWage;
    }

    @Override
    public String toString() {
        return "WagesPost{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", companyId=" + companyId +
                ", isactive=" + isactive +
                ", wagesPostWage=" + wagesPostWage+
                ", PostGrade=" + postGrade.toString() +
                '}';
    }
}
