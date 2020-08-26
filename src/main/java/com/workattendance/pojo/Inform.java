package com.workattendance.pojo;


//通知
public class Inform {
    private int id;
    private String Title;
    private String Content;
    private String Time;
    private String Name;
    private String filedir;
    private String fileName;
    private String rdStatus;
    private String userName;
    private String Password;
    private Integer Type1;
    private Integer Type2;

    public Integer getType1() {
        return Type1;
    }

    public void setType1(Integer type1) {
        Type1 = type1;
    }

    public Integer getType2() {
        return Type2;
    }

    public void setType2(Integer type2) {
        Type2 = type2;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getRdStatus() {
        return rdStatus;
    }

    public void setRdStatus(String rdStatus) {
        this.rdStatus = rdStatus;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFiledir() {
        return filedir;
    }

    public void setFiledir(String filedir) {
        this.filedir = filedir;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    @Override
    public String toString() {
        return "Inform{" +
                "id=" + id +
                ", Title='" + Title + '\'' +
                ", Content='" + Content + '\'' +
                ", Time='" + Time + '\'' +
                ", Name='" + Name + '\'' +
                ", filedir='" + filedir + '\'' +
                ", fileName='" + fileName + '\'' +
                ", rdStatus='" + rdStatus + '\'' +
                ", userName='" + userName + '\'' +
                ", Password='" + Password + '\'' +
                ", Type1=" + Type1 +
                ", Type2=" + Type2 +
                '}';
    }
}
