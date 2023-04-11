package com.example.hospital.api.db.dao;

import com.example.hospital.api.db.Entity.DoctorPrescriptionEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.Map;

@Mapper
public interface DoctorPrescriptionDao {
 public void insert(DoctorPrescriptionEntity entity);
 public HashMap searchPrescriptionByRegistrationId(Map param);
}

