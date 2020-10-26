package com.howei.service;

import com.howei.pojo.WorkPerator;

import java.util.List;
import java.util.Map;

public interface WorkPeratorService {
    /**
     * 查询记录
     * @param
     * @return
     */
    public List<WorkPerator> selByUser(Map map);

    public String selByUserCount(Map map);

    /**
     * 添加模板
     * @param workPerator
     * @return
     */
    int addWorkPerator(WorkPerator workPerator);

    int updStatus(Map<String, Object> map);

    void delWorkPerator(Map map);

    String selById(int id);

    List<WorkPerator> getTemplateChildList(Map map);

    int getTemplateChildListCount(Map map);

    WorkPerator selWorkperator(String id);

    int updWorkperator(WorkPerator work);

    int updWorkperatorChild(Map map);

    List<WorkPerator> selAll(Map map);

    List<String> selByParam(Map<String, Object> map);

    int selAllCount(Map<String, Object> map);

    List<Map> getTemplateMap(Map map);
}
