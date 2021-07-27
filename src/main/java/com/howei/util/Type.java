package com.howei.util;

public enum Type {
    //操作成功
    SUCCESS,
    success,
    //操作失败
    ERROR,
    error,
    //操作无效
    CANCEL,
    cancel,
    FAIL,
    //无绩效管理人：请添加绩效管理人后再申请
    AddManager,
    RESIGNIN,
    //存在相同记录
    havaRecord,
    //无用户:用户过期
    noUser,
    //无记录
    noRecord,
    //无参数
    noParameters;


}
