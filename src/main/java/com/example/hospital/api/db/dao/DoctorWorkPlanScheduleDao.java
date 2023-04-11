package com.example.hospital.api.db.dao;

import com.example.hospital.api.db.Entity.DoctorWorkPlanScheduleEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Mapper
public interface DoctorWorkPlanScheduleDao {
  public void insert(DoctorWorkPlanScheduleEntity entity);
  public ArrayList<HashMap> searchNewSchedule(int workPlanId);
  public ArrayList<HashMap> searchDeptSubSchedule(Map param);
  public ArrayList<HashMap> searchByWorkPlanId(int workPlanId);
  public long searchSumNumByIds(ArrayList<Integer> ids);
  public void deleteByIds(ArrayList<Integer> ids);
  public void deleteByWorkPlanId(int workPlanId);
  public ArrayList<HashMap> searchDoctorWorkPlanSchedule(Map param);
  public int updateNumById(Map param);
  public int releaseNumByOutTradeNo(String outTradeNo);
}




