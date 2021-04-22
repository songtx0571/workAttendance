package com.howei.pojo;

/**
 * 岗位等级
 */
public class PostGrade {
    private Integer id;
    private Integer wagesPostId;//岗位Id
    private String created;
    private String gradeName;//等级名称
    private double postGradeWage;//岗位等级工资

    //虚字段
    private String wagesPostName;//岗位名称
    private Integer index;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public double getPostGradeWage() {
        return postGradeWage;
    }

    public void setPostGradeWage(double postGradeWage) {
        this.postGradeWage = postGradeWage;
    }

    public String getGradeName() {
        return gradeName;
    }

    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
    }

    public Integer getWagesPostId() {
        return wagesPostId;
    }

    public void setWagesPostId(Integer wagesPostId) {
        this.wagesPostId = wagesPostId;
    }

    public String getWagesPostName() {
        return wagesPostName;
    }

    public void setWagesPostName(String wagesPostName) {
        this.wagesPostName = wagesPostName;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    @Override
    public String toString() {
        return "PostGrade{" +
                "id=" + id +
                ", wagesPostId=" + wagesPostId +
                ", created='" + created + '\'' +
                ", gradeName='" + gradeName + '\'' +
                ", PostGradeWage=" + postGradeWage +
                ", wagesPostName='" + wagesPostName + '\'' +
                '}';
    }
}
