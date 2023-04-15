package com.example.hospital.api.db.dao;

import com.example.hospital.api.db.Entity.RateEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.HashMap;

@Mapper
public interface RateDao {
    public int insert(RateEntity entity);
    public ArrayList<HashMap> searchByUserIdAll(Integer userId);
    public HashMap searchByRateId(Integer rateId);
    public void addLikes(Integer rateId);
}
