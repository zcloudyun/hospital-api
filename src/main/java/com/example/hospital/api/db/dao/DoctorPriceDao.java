package com.example.hospital.api.db.dao;

import org.apache.commons.collections.map.HashedMap;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Mapper
public interface DoctorPriceDao {
   public ArrayList<HashMap> searchByPagePrice(Map param);
   public long searchCount(Map param);
   public void updatePriceById(Map param);
}




