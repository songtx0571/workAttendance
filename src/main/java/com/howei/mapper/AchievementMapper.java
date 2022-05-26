package com.howei.mapper;

import com.howei.pojo.Achievement;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AchievementMapper {
    List<Achievement> select();
}
