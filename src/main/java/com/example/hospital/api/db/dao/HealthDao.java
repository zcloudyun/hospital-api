package com.example.hospital.api.db.dao;

import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.HashMap;

@Mapper
public interface HealthDao {
  public ArrayList<HashMap> searchAll();
  public void updateByHealthId(int health_id);
}
