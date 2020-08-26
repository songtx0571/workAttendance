package com.workattendance.pojo;


public class MaintenanceRecord {

    private int id;

    private int maintenanceId;

    private int type;

    private String content;

    private String datetime;

    private String people;
    private String peopleName;

    private double workingHours;

    private String defectNumber;

    private String userName;

    private String Name;

    public MaintenanceRecord(int id, int maintenanceId, int type, String content) {
        super();
        this.id = id;
        this.maintenanceId = maintenanceId;
        this.type = type;
        this.content = content;
    }

    public MaintenanceRecord() {
        super();
    }

    @Override
    public String toString() {
        return "MaintenanceRecord{" +
                "id=" + id +
                ", maintenanceId=" + maintenanceId +
                ", type=" + type +
                ", content='" + content + '\'' +
                ", datetime='" + datetime + '\'' +
                ", people='" + people + '\'' +
                ", peopleName='" + peopleName + '\'' +
                ", workingHours=" + workingHours +
                ", defectNumber='" + defectNumber + '\'' +
                ", userName='" + userName + '\'' +
                ", Name='" + Name + '\'' +
                '}';
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMaintenanceId() {
        return maintenanceId;
    }

    public void setMaintenanceId(int maintenanceId) {
        this.maintenanceId = maintenanceId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getPeople() {
        return people;
    }

    public void setPeople(String people) {
        this.people = people;
    }

    public double getWorkingHours() {
        return workingHours;
    }

    public void setWorkingHours(double workingHours) {
        this.workingHours = workingHours;
    }

    public String getDefectNumber() {
        return defectNumber;
    }

    public void setDefectNumber(String defectNumber) {
        this.defectNumber = defectNumber;
    }

    public String getPeopleName() {
        return peopleName;
    }

    public void setPeopleName(String peopleName) {
        this.peopleName = peopleName;
    }

}
