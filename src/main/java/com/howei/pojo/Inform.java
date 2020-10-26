package com.howei.pojo;


//通知
public class Inform {
    private int id;
    private String title;//标题
    private String content;//内容主体
    private String created;
    private int createdBy;
    private String filedir;//文件地址
    private int informTypeId;//类型id
    private int companyId;//公司id

    //虚字段
    private String companyName;
    private String InformTypeName;
    private String createdByName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreated() {
        return created;
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

    public String getFiledir() {
        return filedir;
    }

    public void setFiledir(String filedir) {
        this.filedir = filedir;
    }

    public int getInformTypeId() {
        return informTypeId;
    }

    public void setInformTypeId(int informTypeId) {
        this.informTypeId = informTypeId;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getInformTypeName() {
        return InformTypeName;
    }

    public void setInformTypeName(String informTypeName) {
        InformTypeName = informTypeName;
    }

    public String getCreatedByName() {
        return createdByName;
    }

    public void setCreatedByName(String createdByName) {
        this.createdByName = createdByName;
    }
}