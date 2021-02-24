package com.howei.service;

import com.howei.pojo.Reimbursement;

import java.util.List;
import java.util.Map;

public interface ReimbursementService {

    List<Reimbursement> getReimbursementList(Map map);

    int addReimbursement(Reimbursement reimbursement);

    Reimbursement getReimbursementById(String id);

    int updReimbursement(Reimbursement reimbursement);

    int examine(Reimbursement reimbursement);
}
