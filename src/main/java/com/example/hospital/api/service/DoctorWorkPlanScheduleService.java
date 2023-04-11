package com.example.hospital.api.service;

import com.example.hospital.api.db.Entity.DoctorWorkPlanScheduleEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public interface DoctorWorkPlanScheduleService {
//    添加出诊时间
    public void insert(ArrayList<DoctorWorkPlanScheduleEntity> list);
    public ArrayList searchDeptSubSchedule(Map param);
    public HashMap searchByWorkPlanId(int workPlanId);
    public void updateSchedule(Map param);
    public void deleteByWorkPlanId(int workPlanId);
}
