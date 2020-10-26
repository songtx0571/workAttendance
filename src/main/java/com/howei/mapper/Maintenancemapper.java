package com.howei.mapper;

import com.howei.pojo.Maintenance;
import com.howei.pojo.MaintenanceRecord;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Component
public interface Maintenancemapper {
    Maintenance getMaintenanceByProject(@Param("datetime") String datetime, @Param("project") int project);

    Maintenance getMaintenanceById(@Param("id") int id);

    Maintenance[] getMaintenances(@Param("project") int project);

    MaintenanceRecord[] getMaintenanceRecords(@Param("datetime") String datetime, @Param("project") int project);

    MaintenanceRecord getMaintenanceRecord(@Param("id") int id);

    MaintenanceRecord[] getMaintenanceRecordsByMaintenanceId(@Param("maintenanceId") int maintenanceId);

    MaintenanceRecord[] getMaintenanceRecordsByMaintenanceId1(@Param("maintenanceId") int maintenanceId);

    String getAttendance(@Param("maintenanceId") int maintenanceId);

    int delMaintenance(@Param("id") int id);

    int updateMaintenanceTotalTime(@Param("totalTime") double totalTime, @Param("id") int id);

    int updateMaintenanceAttendance(@Param("attendance") int attendance, @Param("id") int id);

    int updateMaintenance(@Param("maintenance") Maintenance maintenance);

    int insertMaintenance(@Param("maintenance") Maintenance maintenance);

    int insertMaintenanceRecord(@Param("maintenanceRecord") MaintenanceRecord maintenanceRecord);

    int insertMaintenanceRecordALL(@Param("maintenanceRecord") MaintenanceRecord maintenanceRecord);

    int updateMaintenanceRecord(@Param("maintenanceRecord") MaintenanceRecord maintenanceRecord);

    int updateMaintenanceRecordALL(@Param("maintenanceRecord") MaintenanceRecord maintenanceRecord);

    int updateMaintenanceRecordALL1(@Param("maintenanceRecord") MaintenanceRecord maintenanceRecord);

    int delMaintenanceRecord(@Param("id") int id);

    int delMaintenanceRecordByMaintenanceId(@Param("MaintenanceId") int MaintenanceId);

    int insertMaintenanceByLeader(@Param("maintenance") Maintenance maintenance);

    int addLeader(@Param("maintenance") Maintenance maintenance);
}
