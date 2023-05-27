package com.example.hospital.api.db.dao;

import com.example.hospital.api.db.Entity.DoctorWorkPlanEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Mapper
public interface DoctorWorkPlanDao {
     public ArrayList<HashMap> searchWorkPlanInRange(Map param);
     public Integer searchId(Map param);
     @Transactional
     public int insert(DoctorWorkPlanEntity entity);
     public void updateMaximum(Map param);
     public Integer searchNumById(int id);
     public void deleteById(int id);

     public ArrayList<String> searchCanRegisterInDateRange(Map param);
     public ArrayList<HashMap> searchDeptSubDoctorPlanInDay(Map param);
     public int updateNumById(Map param);
     public int releaseNumByOutTradeNo(String outTradeNo);
     public HashMap searchDoctorMessage(int userId);
}




