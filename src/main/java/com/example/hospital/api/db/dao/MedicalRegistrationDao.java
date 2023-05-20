package com.example.hospital.api.db.dao;

import com.example.hospital.api.db.Entity.MedicalRegistrationEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Mapper
public interface MedicalRegistrationDao {
    public long searchRegistrationCountInToday(Map param);
    public Integer hasRegisterRecordInDay(Map param);
    public int insert(MedicalRegistrationEntity entity);

    public int discardPayment(String outTradeNo);
    public HashMap searchWorkPlanIdAndScheduleId(String outTradeNo);
    public boolean updatePayment(Map param);
    public String searchPaymentResult(String outTradeNo);
    public HashMap searchRegistrationInfo(Map param);
    public ArrayList<HashMap> searchByStatus(Map param);
    public int searchDoctorId(int userId);
    public void updateStatus(Integer id);
    public HashMap searchRegistrationById(Map param);

    public ArrayList<HashMap> searchByUserId(Map param);
    public Boolean updateByRegistrationId(Integer registrationId);
    public void updateEvaluate(Integer registrationId);
}




