package com.example.hospital.api.db.dao;

import com.example.hospital.api.db.Entity.FaceAuthEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

@Mapper
public interface FaceAuthDao {
  public Integer hasFaceAuthInDay(Map param);
  public int insert(FaceAuthEntity entity);
}




