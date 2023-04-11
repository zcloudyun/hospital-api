package com.example.hospital.api.service;

import com.example.hospital.api.common.PageUtils;
import com.example.hospital.api.db.Entity.MedicalRecordEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public interface MedicalRecordService {
    public void insert(Map param);
    public PageUtils searchAll(Map param);
    public void deleteIds(Integer[] ids);
    public PageUtils searchPrescription(Map param);
    public void deleteByIds(Integer[] ids);
}
