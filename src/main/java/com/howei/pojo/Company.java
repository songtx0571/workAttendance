package com.howei.pojo;

/**
 * 公司
 */
public class Company {

    private int id;
    private String name;
    private String created;
    private int createdBy;
    private int isactive;
    private int parent;
    private String codeName;//代号
    private int headQuarters;//标识是否为本部

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

    public int getIsactive() {
        return isactive;
    }

    public void setIsactive(int isactive) {
        this.isactive = isactive;
    }

    public int getParent() {
        return parent;
    }

    public void setParent(int parent) {
        this.parent = parent;
    }

    public String getCodeName() {
        return codeName;
    }

    public void setCodeName(String codeName) {
        this.codeName = codeName;
    }

    public int getHeadQuarters() {
        return headQuarters;
    }

    public void setHeadQuarters(int headQuarters) {
        this.headQuarters = headQuarters;
    }
}
