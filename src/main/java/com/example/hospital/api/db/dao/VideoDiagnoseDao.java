package com.example.hospital.api.db.dao;

import com.example.hospital.api.db.Entity.VideoDiagnoseEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;

@Mapper
public interface VideoDiagnoseDao {
    public void insert(VideoDiagnoseEntity entity);
    public HashMap searchByOutTradeNo(String outTradeNo);
}




