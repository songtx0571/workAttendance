package com.howei.mapper;

import com.howei.pojo.Reimbursement;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public interface ReimbursementMapper {

    List<Reimbursement> getReimbursementList(Map map);

    int addReimbursement(Reimbursement reimbursement);

    Reimbursement getReimbursementById(@Param("id") String id);

    int updReimbursement(Reimbursement reimbursement);

    int examine(Reimbursement reimbursement);

    List<Map> getReimbuseStatistic(Map map);
}
