package com.example.hospital.api.db.dao;

import com.example.hospital.api.db.Entity.InspectEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Mapper
public interface InspectDao {
   public ArrayList<HashMap> searchAll();
   public ArrayList<HashMap> searchByPage(Map param);
   public long inspectCount(Map param);
   public int insert(InspectEntity entity);
   public void update(Map param);
   public void deleteByIds(Integer[] ids);
}
