package com.howei.config;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionIdGenerator;

import java.io.Serializable;

public class UUIDSessionIdGenerator implements SessionIdGenerator {

    @Override
    public Serializable generateId(Session session) {
        // TODO Auto-generated method stub
        Serializable id = "123456";
        return id;
    }
}
