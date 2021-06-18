package com.howei.pojo;

public class OverhaulRecord {

    private Integer employeeId;
    private String userNumber;
    private String userName;
    private String finishDateTime;
    private String finishYearMonth;
    private String finishDay;
    private Double workingHour;
    private Double overtime;

    @Override
    public String toString() {
        return "OverhaulRecord{" +
                "employeeId=" + employeeId +
                ", userNumber='" + userNumber + '\'' +
                ", userName='" + userName + '\'' +
                ", finishDateTime='" + finishDateTime + '\'' +
                ", finishYearMonth='" + finishYearMonth + '\'' +
                ", finishDay='" + finishDay + '\'' +
                ", workingHour=" + workingHour +
                ", overtime=" + overtime +
                '}';
    }

    public Double getOvertime() {
        return overtime;
    }

    public void setOvertime(Double overtime) {
        this.overtime = overtime;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public String getUserNumber() {
        return userNumber;
    }

    public void setUserNumber(String userNumber) {
        this.userNumber = userNumber;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFinishDateTime() {
        return finishDateTime;
    }

    public void setFinishDateTime(String finishDateTime) {
        this.finishDateTime = finishDateTime;
    }

    public String getFinishYearMonth() {
        return finishYearMonth;
    }

    public void setFinishYearMonth(String finishYearMonth) {
        this.finishYearMonth = finishYearMonth;
    }

    public String getFinishDay() {
        return finishDay;
    }

    public void setFinishDay(String finishDay) {
        this.finishDay = finishDay;
    }

    public Double getWorkingHour() {
        return workingHour;
    }

    public void setWorkingHour(Double workingHour) {
        this.workingHour = workingHour;
    }
}
