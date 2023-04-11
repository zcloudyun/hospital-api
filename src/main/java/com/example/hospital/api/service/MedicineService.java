package com.example.hospital.api.service;

import com.example.hospital.api.common.PageUtils;
import com.example.hospital.api.db.Entity.MedicineEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public interface MedicineService {
    public PageUtils searchByPage(Map param);

    public void update(Map param);
    public void insert(Map param);
    public ArrayList<HashMap> searchAll();
}
