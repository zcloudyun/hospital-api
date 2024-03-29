package com.example.hospital.api.db.dao;

import com.example.hospital.api.db.Entity.MedicalRecordEntity;
import com.example.hospital.api.db.Entity.PerscriptionEntity;
import com.example.hospital.api.db.Entity.PrescriptionEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface MedicalRecordDao {
    public void insert(MedicalRecordEntity entity);
    public ArrayList<HashMap> searchAll(Map param);
    public long searchAllCount(Map param);
    public void deleteIds(Integer[] ids);
    public List<PerscriptionEntity> searchPrescription(Map param);
    public long searchPrescriptionCount(Map param);
    public void deleteByIds(Integer[] ids);
    public ArrayList<HashMap>searchByUserId(Integer userId);
    public ArrayList<HashMap>searchRpById(Integer[] ids);
    public ArrayList<HashMap> searchByUserIdRecord(Integer userId);
    public HashMap searchByRecordId(Integer recordId);
    public List<PrescriptionEntity> searchMedicine(int patientId);
    public long MedicineCount(int patientId);
}
