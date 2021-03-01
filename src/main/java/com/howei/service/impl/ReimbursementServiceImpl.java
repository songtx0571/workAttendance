package com.howei.service.impl;

import com.howei.mapper.ReimbursementMapper;
import com.howei.pojo.Reimbursement;
import com.howei.service.ReimbursementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ReimbursementServiceImpl implements ReimbursementService {

    @Autowired
    private ReimbursementMapper reimbursementMapper;

    @Override
    public List<Reimbursement> getReimbursementList(Map map) {
        return reimbursementMapper.getReimbursementList(map);
    }

    @Override
    public int addReimbursement(Reimbursement reimbursement) {
        return reimbursementMapper.addReimbursement(reimbursement);
    }

    @Override
    public Reimbursement getReimbursementById(String id) {
        return reimbursementMapper.getReimbursementById(id);
    }

    @Override
    public int updReimbursement(Reimbursement reimbursement) {
        return reimbursementMapper.updReimbursement(reimbursement);
    }

    @Override
    public int examine(Reimbursement reimbursement) {
        return reimbursementMapper.examine(reimbursement);
    }

    @Override
    public List<Map> getReimbuseStatistic(Map map) {
        return reimbursementMapper.getReimbuseStatistic(map);
    }
}
