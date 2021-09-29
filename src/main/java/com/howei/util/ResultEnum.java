package com.howei.util;

public enum ResultEnum {

    SERVICE_EXCEPTION(500, "服务器异常"),
    OK(0, "操作成功"),
    NO_USER(201, "用户过期,请重新登录"),
    FAIL(202, "操作失败"),
    CANCEL(203, "操作已取消"),
    ADD_MANAGER_ERROR(204, "无绩效管理人,请添加绩效管理人后再申请"),
    REJECT(205, "操作被拒绝"),
    HAVE_RECORD(206, "存在相同记录"),
    NO_RECORD(207, "无记录"),
    NO_PARAMETERS(208, "缺少参数"),
    DEFECT_NOT_STARTED(209, "缺陷计时已暂停,请联系班长开始计时缺陷"),
    NO_PERMISSION(210, "没有权限"),
    NO_DEPARTMENT_ID(211, "当前部门无编号"),
    DEFECT_NO_CLAIM_TIMEOUT(212, "该缺陷没有认领超时"),
    DEFECT_NO_START_TIMEOUT(213, "该缺陷没有开始超时"),
    DEFECT_NO_FEEDBACK_TIMEOUT(214, "该缺陷没有反馈超时"),
    DEFECT_NO_CHECK_TIMEOUT(215, "该缺陷没有验收超时"),
    DEFECT_NO_END_TIMEOUT(216, "该缺陷没有结束超时"),
    DEFECT_NO_HANDLE_TIMEOUT(216, "该缺陷没有处理超时"),
    MAINTAIN_DISTRIBUTED(217, "该维护配置已经被分配"),
    MAINTAIN_STOPED(218, "该维护配置已经暂停"),
    WORKPERATOR_HAVE_SAME_RECORD(219, "已存在同名的配置模板"),
    POSTPERATOR_OPEN(220, "OPEN"),
    POSTPERATOR_CONSOLE(221, "该检修已经完成,请等候下一周期到来"),
    DEFECT_TWO_START_EXECUTING(222, "已有两条缺陷正在执行,请先完成它们"),
    HAVE_RECORD_TO_OVERWRITE(223, "记录已经存在,是否覆盖他们"),
    HAVE_NO_RECORD(224, "当前没有记录"),
    TO_ADD_MANAGER(225, "无绩效管理人：请添加绩效管理人后再申请");

    private Integer code;
    private String msg;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    ResultEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
