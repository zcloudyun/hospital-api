package com.example.hospital.api.db.dao;

import com.example.hospital.api.db.Entity.InspectResultEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface InspectResultDao {
    public int insert(InspectResultEntity entity);
}
