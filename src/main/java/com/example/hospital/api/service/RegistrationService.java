package com.example.hospital.api.service;

import com.example.hospital.api.db.Entity.VideoDiagnoseEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public interface RegistrationService {
    public ArrayList<HashMap> searchCanRegisterInDateRange(Map param);
    public ArrayList<HashMap> searchDeptSubDoctorPlanInDay(Map param);

    public String checkRegisterCondition(Map param);

    public ArrayList<HashMap> searchDoctorWorkPlanSchedule(Map param);
    public HashMap registerMedicalAppointment(Map param);

    public boolean updatePayment(Map param);
    public boolean searchPaymentResult(String outTradeNo);

    public HashMap searchRegistrationInfo(Map param);
    public ArrayList<HashMap> searchByStatus(Integer status);
    public HashMap searchRegistrationById(Map param);

    public ArrayList<HashMap> searchByUserId(Map param);

    public Boolean updateByRegistrationId(Integer registrationId);
}
