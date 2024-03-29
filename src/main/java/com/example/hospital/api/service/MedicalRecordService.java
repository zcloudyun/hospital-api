package com.example.hospital.api.service;

import com.example.hospital.api.common.PageUtils;
import com.example.hospital.api.db.Entity.MedicalRecordEntity;
import com.example.hospital.api.db.Entity.MedicineEntity;
import com.example.hospital.api.db.Entity.PerscriptionEntity;
import com.example.hospital.api.db.Entity.PrescriptionEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface MedicalRecordService {
    public void insert(Map param);
    public PageUtils searchAll(Map param);
    public void deleteIds(Integer[] ids);
    public PageUtils searchPrescription(Map param);
    public void deleteByIds(Integer[] ids);
    public ArrayList<HashMap> searchByUserIdRecord(Integer userId);
    public HashMap searchByRecordId(Integer recordId);
    public List<PrescriptionEntity> searchMedicine(int patientId);
}
