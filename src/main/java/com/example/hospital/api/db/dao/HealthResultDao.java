package com.example.hospital.api.db.dao;

import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Mapper
public interface HealthResultDao {
    public HashMap searchByType(Map param);
    public ArrayList<HashMap> searchRecommed(int health_id);
    public ArrayList<HashMap> searchScale(int health_id);
}
