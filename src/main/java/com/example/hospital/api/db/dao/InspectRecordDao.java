package com.example.hospital.api.db.dao;

import com.example.hospital.api.db.Entity.InspectRecordEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Mapper
public interface InspectRecordDao {
  public int insert(InspectRecordEntity entity);
  public ArrayList<HashMap> searchByStatus(Map param);
  public void updateStatus(int recordId);
  public ArrayList<HashMap> searchInspectRecordByStatus(Map param);
  public ArrayList<HashMap> searchRecordAll(int userId);
  public HashMap searchbyRecordId(int recordId);
}
