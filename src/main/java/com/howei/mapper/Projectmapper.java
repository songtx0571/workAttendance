package com.howei.mapper;

import com.howei.pojo.Salary;
import com.howei.pojo.ProjectTeam;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface Projectmapper extends Commonmapper {
    List<ProjectTeam> find();

    List<Salary> getCoe(@Param("cycle") String cycle);

    List<Salary> getCoe2(@Param("cycle") String cycle);

    List<Salary> updateXishu(@Param("cycle") String cycle, @Param("canbuxishu") String canbuxishu, @Param("jiabanxishu") String jiabanxihsu);

    List<Salary> setCanbu(@Param("canbuxishu") Double canbuxishu);
}
