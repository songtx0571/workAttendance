package com.workattendance.mapper;

import com.workattendance.pojo.WorkPerator;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public interface WorkPeratorMapper {

    public List<WorkPerator> selByUser(Map map);

    public String selByUserCount(Map map);

    int addWorkPerator(WorkPerator workPerator);

    int updStatus(Map<String, Object> map);

    void delWorkPerator(Map map);

    String selById(@Param("id") int id);

    List<WorkPerator> getTemplateChildList(Map map);

    WorkPerator selWorkperator(String id);

    int updWorkperator(WorkPerator work);

    int updWorkperatorChild(Map map);

    List<WorkPerator> selAll(Map map);

    List<String> selByParam(Map<String, Object> map);

    int selAllCount(Map<String, Object> map);

    int getTemplateChildListCount(Map map);

    List<Map> getTemplateMap(Map map);
}
